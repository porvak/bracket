function (gameKey) {
    var totalPoints = 0;
    var userTournament = db.userTournament.findOne({userId:gameKey.userId, tournamentId:gameKey.tournamentId});

    // Loop through the regions
    var regions = userTournament.regions;
    for (var regionId in regions) {
        var region = regions[regionId];

        // Loop through the rounds
        var rounds = region.rounds;
        for (var round in rounds) {
            var dbRoundId = rounds[round].dbRoundId;
            if (dbRoundId == gameKey.roundId) {
                var games = rounds[round].games
                for (var gameId in games) {
                    // Sum up the possible points per round
                    totalPoints += pointsInRound(gameKey.poolId, dbRoundId);
                }
            }
        }
    }

    return totalPoints;
}