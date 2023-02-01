/**
 * Program made by Tomy (https://osu.ppy.sh/users/14889628 / https://github.com/TomyDoesThings)
 * This program was made to test out how well a percentile based ranking system would work in osu!.
 */

import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.net.URL;

public class osuUserPercentileFinder
{
    public static void main(String[] args) throws IOException // Throwing it seems like the way to go for file creation. Please tell me if there's another way. :<
    {
        JOptionPane.showMessageDialog(null, "Welcome to Tomy's Percentile Finder!");
        JOptionPane.showMessageDialog(null, "Tax Evasion formerly known as mxkvl gave me the idea to do this" +
                "\nand perhaps it could inspire Ephemeral and/or more of the osu! team to continue looking into this, hopefully, please...?");

        int ranking = 0;
        String mode = "";

        URL currentURL;

        try
        {
            do
            {
                mode = JOptionPane.showInputDialog("Enter o for osu!, t for Taiko, m for Mania, or c for Catch The Beats.");
            } while (!checkMode(mode));
        } catch (NullPointerException e)
        {
            JOptionPane.showMessageDialog(null, "No mode was entered. (Perhaps you closed the program with no input.) The program will now close.");
            System.exit(0);
        }
        mode = switch (mode)
                {
                    case "o" -> "osu";
                    case "t" -> "taiko";
                    case "c" -> "fruits";
                    default -> "mania";
                };

        try
        {
            while (ranking < 1) // Reminder that ranking is initially set to 0 which makes this while loop work
            {
                ranking = Integer.parseInt(JOptionPane.showInputDialog(null, "What ranking is this player for " + mode + "? (integer only, no commas)"));
                if (ranking < 1)
                {
                    JOptionPane.showMessageDialog(null, "You can't have a rank better than #1, silly.");
                }
            }
        } catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog(null, "Java doesn't think that's an integer. The program will now close.");
            System.exit(0);
        }

        String nameOfOutputFile = JOptionPane.showInputDialog("What do you want the name of the txt output file to be? .txt will be added to the end automatically.\nWARNING: MATCHING NAMES WILL CAUSE THE EXISTING FILE TO BE OVERWRITTEN/DELETED/ERASED/ERADICATED/OBLITERATED/DESTROYED/F***ED! I'M NOT RESPONSIBLE FOR ANY DATA LOSS!");

        int confirmationInteger = JOptionPane.showConfirmDialog(null, "Mode: " + mode + "\nRank: " + ranking + "\nName of Output File: " + nameOfOutputFile + ".txt\nAre these options you chose alright for you?\nNote: Will require relaunch in order to enter proper settings.");
        if (confirmationInteger != 0) // Cancel is 2, No is 1, Yes is 0
        {
            JOptionPane.showMessageDialog(null, "The program will now close.\nFeel free to relaunch to enter the proper settings.");
            System.exit(0);
        }

        JOptionPane.showMessageDialog(null, "Please wait momentarily.");
        FileWriter myWriter;
        myWriter = new FileWriter(nameOfOutputFile + ".txt");

        final String activeUsersIdentifier = "v>                    </td>                    <td class=\"ranking-page-table__column ranking-page-table__column--dimmed\">                        ";
        Scanner scnr;
        String htmlText = "";
        int initialPage = 1; // Initial page is always 1. If this changes on osu! site, something big happened.
        int finalPage;
        try
        {
            currentURL = new URL("https://osu.ppy.sh/rankings/" + mode + "/country?page=" + initialPage + "#scores");
            scnr = new Scanner(currentURL.openStream());
            while (scnr.hasNextLine())
            {
                htmlText += scnr.nextLine();
            }
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Something (MalformedURLException or IOException) went wrong in trying to fetch " + "https://osu.ppy.sh/rankings/" + mode + "/country" + ".\n The program will now close.");
        }

        finalPage = (countMatches(htmlText, "page=") / 2) + initialPage; // Remaining pages 2 (or nonexistent) to something shows on top and bottom of site. Divide by 2 to get remaining pages to add to initial page. That's how this code works.

        if (finalPage > initialPage)
        {
            for (int i = initialPage + 1; i <= finalPage; ++i)
            {
                try
                {
                    currentURL = new URL("https://osu.ppy.sh/rankings/" + mode + "/country?page=" + i + "#scores");
                    scnr = new Scanner(currentURL.openStream());
                    while (scnr.hasNextLine())
                    {
                        htmlText += scnr.nextLine();
                    }
                } catch (Exception e)
                {
                    JOptionPane.showMessageDialog(null, "Something (MalformedURLException or IOException) went wrong in trying to fetch " + "https://osu.ppy.sh/rankings/" + mode + "/country" + ".\n The program will now close.");
                }
            }
        }
        myWriter.write("ALL HTML TEXT USED IN FINDING PERCENTILE IS FROM THE FOLLOWING:\n");
        myWriter.write(htmlText);
        myWriter.write("\nTHAT'S ALL THE HTML TEXT.\n\n\n\n\n");
        myWriter.flush();

        myWriter.write("Information computed:\n");

        int amountOfCountries = countMatches(htmlText, activeUsersIdentifier);
        int allActiveUsers = 0;

        int currentCountry = 1;

        int currentInstanceIndex = htmlText.indexOf(activeUsersIdentifier);
        String currentCountryActiveUsers = "";

        while (currentInstanceIndex != -1)
        {
            currentInstanceIndex += activeUsersIdentifier.length();

            while (Character.isDigit(htmlText.charAt(currentInstanceIndex)) || htmlText.charAt(currentInstanceIndex) == ',')
            {
                if (Character.isDigit(htmlText.charAt(currentInstanceIndex)))
                {
                    currentCountryActiveUsers += htmlText.charAt(currentInstanceIndex);
                }
                ++currentInstanceIndex;
            }

            myWriter.write("Country #" + currentCountry + " - " + currentCountryActiveUsers + "\n");
            myWriter.flush();
            ++currentCountry;
            allActiveUsers += Integer.parseInt(currentCountryActiveUsers);
            currentCountryActiveUsers = "";
            currentInstanceIndex = htmlText.indexOf(activeUsersIdentifier, (currentInstanceIndex + 1));
        }

        double percentilePercent = ((double) ranking/allActiveUsers) * 100;

        myWriter.write("\nFor a player who is rank #" + ranking + " in " + mode + ", they are in the " + ranking + "/" + allActiveUsers + " percentile of active " + mode + " players from every country osu! recognizes (" + amountOfCountries + ") found. (confirm the amount of countries to ensure there was no bug)" +
                "\nSimplified, they are approximately in the top " + percentilePercent + "% percentile of active players who play " + mode + "." +
                "\nBig thanks to Tax Evasion (mxkvl or max osu) and Ephemeral for pushing me to make this program to showcase the concept applied to osu!.\n\nI hope this program helped you. ;)\nRemember, this way of looking at ranking is conceptual. I'd say it's very cool though.\nA completely different way to look at people's ranking.\n\nThe program will now close.");
        myWriter.close();

        if (ranking > allActiveUsers)
        {
            JOptionPane.showMessageDialog(null, "To let you know, your ranking number is a bigger number than the total amount of active players." +
                    "\nSomething isn't right with that, but this program has still computed the data regardless.");
        }

        JOptionPane.showMessageDialog(null, "For a player who is rank #" + ranking + " in " + mode + ", they are in the " + ranking + "/" + allActiveUsers + " percentile of active " + mode + " players from every country osu! recognizes (" + amountOfCountries + ") found. (confirm the amount of countries to ensure there was no bug)" +
                "\nSimplified, they are approximately in the top " + percentilePercent + "% percentile of active players who play " + mode + ".");
        JOptionPane.showMessageDialog(null, "Big thanks to Tax Evasion (mxkvl or max osu) and Ephemeral for pushing me to make this program to showcase the concept applied to osu!.\n\nI hope this program helped you. ;)\nRemember, this way of looking at ranking is conceptual. I'd say it's very cool though.\nA completely different way to look at people's ranking.\n\nThe program will now close.");
    }

    public static boolean checkMode(String mode)
    {
        String[] validModes = {"o", "t", "m", "c"};
        boolean isValid = false;

        for (String validMode : validModes)
        {
            if (mode.equalsIgnoreCase(validMode))
            {
                isValid = true;
                break;
            }
        }

        return isValid;
    }

    public static int countMatches(String text, String str) // gotten from https://www.techiedelight.com/find-occurrences-of-substring-string-java/
    {
        Matcher matcher = Pattern.compile(str).matcher(text);

        int count = 0;
        while (matcher.find())
        {
            count++;
        }

        return count;
    }
}
