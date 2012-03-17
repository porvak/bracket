function() {
  // Keep track of losing teams to weed out future results
  var loserList = new Array();

  // Loop through the regions
  var regions = this.regions;
  for (var regionId in regions) {
    var region = regions[regionId];

    // Loop through the rounds
    var rounds = region.rounds;
    for (var roundId in rounds) {
      var round = rounds[roundId];
      var dbRoundId = round.dbRoundId;

      // Figure out the max points for this pool's round
      var pointsForRound = pointsInRound(this.poolId, dbRoundId);

      // Don't deal with a 0 point round
      if (pointsForRound > 0) {
        // Loop through the games
        var games = round.games
        for (var gameId in games) {
          var game = games[gameId];

          // Set the mapping key
          var gameKey = {
            userId: this.userId,
            poolId: this.poolId,
            tournamentId: this.tournamentId,
            roundId: dbRoundId
          };

          // Assume we have no idea on the success
          var weWon = null;

          // Check if we picked for this games
          var pickedTeam = game.userPickedGameWinner;

          if (pickedTeam != null) {
            // Before going further, check if our pick already lost
            if (loserList.indexOf(pickedTeam.teamId) != -1) {
              weWon = false;
            }
            else if (game.status == "COMPLETE") {
              var winningTeamId;

              // Loop through the teams and find the winner
              var teams = game.teams;
              for (var teamId in teams) {
                var team = teams[teamId];
                if (team.winner) winningTeamId = team._id;
              }

              // Check if we won
              if (winningTeamId != null) {
                weWon = winningTeamId.equals(pickedTeam._id);
                // If not, put the team on the loser list
                if (!weWon) loserList.push(pickedTeam._id);
              }
            }
          }

          var maxPossible = maxPointsInRound(gameKey);
          if (weWon == null) {
            // If we don't know abou this game, no win, no loss
            emit(gameKey, {
              score: 0,
              pointsCounted: 0,
              maxPointsPossible: maxPossible
            });
          } else if (weWon) {
            // If we won, take the points
            emit(gameKey, {
              score: pointsForRound,
              pointsCounted: pointsForRound,
              maxPointsPossible: maxPossible
            });
          } else {
            // If we lost, score nothing
            emit(gameKey, {
              score: 0,
              pointsCounted: pointsForRound,
              maxPointsPossible: maxPossible
            });
          }
        }
      }
    }
  }
}