(function() {

  define(['lib/jquery', 'lib/underscore', 'lib/handlebars', 'app/model/TournamentModel', 'app/model/TeamModel', 'app/view/TeamView', 'text!html/sectionTemplate.html', 'text!html/gameTemplate.html'], function($, _, handlebars, TournamentModel, TeamModel, TeamView, strSectionTemplate, strGameTemplate) {
    return {
      init: function() {
        this.model = new TournamentModel;
        this.model.bind('change', this.render, this);
        this.model.fetch();
        this.teamViews = {};
        this.dropViews = [];
        this.sectionHB = handlebars.compile(strSectionTemplate);
        return this.gameHB = handlebars.compile(strGameTemplate);
      },
      render: function() {
        var elBracket, _ref,
          _this = this;
        console.log("GET: http://" + (window.location.host + this.model.url()) + "\n\n");
        elBracket = $(this.sectionHB({
          "class": "regions"
        }));
        if ((_ref = this.model.get('regions')) != null) {
          _ref.forEach(function(region) {
            var elRegion, _ref2;
            elRegion = $(_this.sectionHB({
              "class": "region region-" + region.regionId,
              header: region.name
            }));
            if ((_ref2 = region.rounds) != null) {
              _ref2.forEach(function(round) {
                var elRound, _ref3;
                elRound = $(_this.sectionHB({
                  "class": "round round-" + round.roundId
                }));
                if ((_ref3 = round.games) != null) {
                  _ref3.forEach(function(game) {
                    var elGame, _ref4;
                    elGame = $(_this.gameHB(game));
                    if ((_ref4 = game.teams) != null) {
                      _ref4.forEach(function(team) {
                        var teamView, teamZero;
                        team.regionId = region.regionId;
                        team.roundId = round.roundId;
                        team.gameId = game.gameId;
                        team.nextGame = (game.nextGame ? game.nextGame : null);
                        if (team.teamId) team.pickable = true;
                        team.locator = "" + region.regionId + "-" + round.roundId + "-" + game.gameId + "-" + team.position;
                        teamView = new TeamView({
                          model: new TeamModel(team)
                        });
                        teamView.on('drag', _this.teamDrag, _this);
                        teamView.on('dragStop', _this.hideDropZones, _this);
                        teamView.on('drop', _this.teamDrop, _this);
                        _this.teamViews[team.locator] = teamView;
                        teamZero = elGame.find('.detail.team-0');
                        if (teamZero.val()) {
                          return teamView.$el.insertAfter(teamZero);
                        } else {
                          return teamView.$el.insertAfter(elGame.find('.detail.state'));
                        }
                      });
                    }
                    return elRound.append(elGame);
                  });
                }
                return elRegion.append(elRound);
              });
            }
            return elBracket.append(elRegion);
          });
        }
        return $('#bracketNode').append(elBracket);
      },
      teamDrag: function(baseView) {
        return this.dropViews = this.recurNextGames(baseView.model, 'showDropZone');
      },
      teamDrop: function(baseView, ui) {
        var landingView, lastLandingView, pendingViews, postError,
          _this = this;
        this.hideDropZones();
        postError = false;
        pendingViews = [];
        landingView = this.teamViews["" + (ui.draggable.data('locator'))];
        if (this.validDropZone(baseView, landingView)) {
          lastLandingView = _.find(this.dropViews, function(dropView, i) {
            var eachView;
            eachView = _this.dropViews[i];
            eachView.$el.addClass('.saving');
            pendingViews.push({
              baseView: eachView,
              landingView: landingView
            });
            return _.isEqual(baseView, dropView);
          });
        }
        this.chainSaveCallbacks(pendingViews);
        return this.checkBrokenLinks(lastLandingView);
      },
      chainSaveCallbacks: function(pendingViews) {
        var baseView, landingView,
          _this = this;
        baseView = pendingViews[0].baseView;
        landingView = pendingViews[0].landingView;
        return baseView.model.save({
          name: landingView.model.get('name'),
          teamId: landingView.model.get('teamId'),
          seed: landingView.model.get('seed')
        }, {
          wait: true,
          success: function(model, response) {
            console.log("POST: http://" + (window.location.host + model.url()) + "\nJSON:" + (JSON.stringify(model.toJSON())) + "\n\n");
            baseView.$el.removeClass('saving');
            if (pendingViews.length > 1) {
              return _this.chainSaveCallbacks(_.last(pendingViews, pendingViews.length - 1));
            }
          },
          error: function(model, response) {
            pendingViews.forEach(function(viewObj) {
              return viewObj.baseView.$el.removeClass('saving');
            });
            if (response.status === 404) {
              return alert('Please sign in using twitter.');
            }
          }
        });
      },
      hideDropZones: function() {
        if (this.dropViews.length > 0) {
          return this.dropViews.forEach(function(view) {
            return view.hideDropZone();
          });
        } else {
          return this.dropViews = this.recurNextGames(model, 'hideDropZone');
        }
      },
      validDropZone: function(baseView, landingView) {
        if (baseView.model.get('regionId') !== landingView.model.get('regionId')) {
          return false;
        }
        if (baseView.model.get('roundId') <= landingView.model.get('roundId')) {
          return false;
        }
        return _.find(this.dropViews, function(dropView) {
          return _.isEqual(baseView, dropView);
        });
      },
      checkBrokenLinks: function(baseView) {
        var game, i, inNextRound, inPreviousRound, locator, position, region, round, teamId;
        locator = baseView.model.get('locator');
        teamId = baseView.model.get('teamId');
        region = parseInt(locator.substr(0, 1), 10);
        round = parseInt(locator.substr(2, 1), 10);
        game = parseInt(locator.substr(4, 1), 10);
        position = parseInt(locator.substr(6, 1), 10);
        i = 0;
        inNextRound = false;
        while (!inNextRound && i < 17) {
          if (this.teamViews["" + region + "-" + (round + 1) + "-" + i + "-" + 0]) {
            if (teamId === this.teamViews["" + region + "-" + (round + 1) + "-" + i + "-" + 0].model.get('teamId')) {
              inNextRound = true;
            }
            if (teamId === this.teamViews["" + region + "-" + (round + 1) + "-" + i + "-" + 1].model.get('teamId')) {
              inNextRound = true;
            }
          }
          i++;
        }
        if (inNextRound) {
          i = 0;
          inPreviousRound = false;
          while (!inPreviousRound && i < 17) {
            if (this.teamViews["" + region + "-" + (round - 1) + "-" + i + "-" + 0]) {
              if (teamId === this.teamViews["" + region + "-" + (round - 1) + "-" + i + "-" + 0].model.get('teamId')) {
                inPreviousRound = true;
              }
              if (teamId === this.teamViews["" + region + "-" + (round - 1) + "-" + i + "-" + 1].model.get('teamId')) {
                inPreviousRound = true;
              }
            }
            i++;
          }
          if (inPreviousRound) {
            return this.recurNextGames(baseView.model, 'brokenLink', [teamId]);
          }
        }
      },
      recurNextGames: function(model, actionAttr, actionAttrArgs, dropViews) {
        var nextGame, nextGameView,
          _this = this;
        nextGame = model.get('nextGame');
        nextGameView = null;
        dropViews = dropViews || [];
        _.find(this.model.get('regions'), function(region) {
          return _.find(region.rounds, function(round) {
            return _.find(round.games, function(game) {
              if (game.gameId === nextGame.gameId && region.regionId === nextGame.regionId) {
                return nextGameView = _this.teamViews["" + region.regionId + "-" + round.roundId + "-" + game.gameId + "-" + nextGame.position];
              }
            });
          });
        });
        if (nextGameView[actionAttr]) {
          nextGameView[actionAttr].apply(nextGameView, actionAttrArgs);
        }
        if (nextGameView) dropViews.push(nextGameView);
        if (nextGameView && nextGameView.model.get('nextGame')) {
          return this.recurNextGames(nextGameView.model, actionAttr, actionAttrArgs, dropViews);
        } else {
          return dropViews;
        }
      }
    };
  });

}).call(this);
