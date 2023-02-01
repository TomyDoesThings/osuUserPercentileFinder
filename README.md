# osuUserPercentileFinder
Finds the percentile for a certain ranking of an osu! player that plays a certain mode.

Program made by Tomy (https://osu.ppy.sh/users/14889628 / https://github.com/TomyDoesThings).
This program was made to test out how well a percentile based ranking system would work in osu!,
but... primarily to see if this gets the osu! team to think about readjusting the ranking medals
to some sort of a percentile system.

As Ephemeral has said in osu! Medal Hunters Discord server,
"yeah they need to be mode-specific i think
and probably adjusted to reflect active player percentiles
like 50k was not a particularly big achievement in 2014
nowadays however, it's something"

He has also told me that there are no guarantees of those medals changing, but perhaps I could encourage a bit more thinking about this.

This is the best shot I have of showing to people if this really is a good idea or not. I hope this program is of use to you all.

How this program works is it adds up all active players by adding up the active players of each country in
https://osu.ppy.sh/rankings/osu/country?page=x#scores,
https://osu.ppy.sh/rankings/taiko/country?page=x#scores,
https://osu.ppy.sh/rankings/fruits/country?page=x#scores,
or https://osu.ppy.sh/rankings/mania/country?page=x#scores,
depending on the mode that you choose where x is all the pages between the first page and final page.

I made the code public, so you can check it out for yourself if you want. This program only took a me a few hours to write.

Shoutouts to Ephemeral (https://osu.ppy.sh/users/102335) and Tax Evasion formerly known as mxkvl (https://osu.ppy.sh/users/12998330) for
pushing to me to really want answers to if a percentile system could possibly fix these medals before it's too late.
