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
        var landingView, pendingViews, postError,
          _this = this;
        this.hideDropZones();
        postError = false;
        pendingViews = [];
        landingView = this.teamViews["" + (ui.draggable.data('locator'))];
        if (this.validDropZone(baseView, landingView)) {
          _.find(this.dropViews, function(dropView, i) {
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
        return this.chainSaveCallbacks(pendingViews);
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
        var game, locator, position, region, round;
        locator = baseView.model.get('locator');
        region = locator.substr(0, 1);
        round = locator.substr(2, 1);
        game = locator.substr(4, 1);
        position = locator.substr(6, 1);
        this.teamViews["" + region + "-" + round + "-" + iterGame + "-" + iterPosition];
        return this.recurNextGames(baseView.model, 'reset');
      },
      recurNextGames: function(model, actionAttr, dropViews) {
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
        if (nextGameView != null) {
          if (typeof nextGameView[actionAttr] === "function") {
            nextGameView[actionAttr]();
          }
        }
        if (nextGameView) dropViews.push(nextGameView);
        if (nextGameView && nextGameView.model.get('nextGame')) {
          return this.recurNextGames(nextGameView.model, actionAttr, dropViews);
        } else {
          return dropViews;
        }
      }
    };
  });

}).call(this);
