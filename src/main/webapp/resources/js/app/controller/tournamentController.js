(function() {
  var __bind = function(fn, me){ return function(){ return fn.apply(me, arguments); }; };
  define(['lib/jquery', 'lib/handlebars', 'app/model/TournamentModel', 'app/model/TeamModel', 'app/view/TeamView', 'text!html/sectionTemplate.html', 'text!html/gameTemplate.html'], function($, handlebars, TournamentModel, TeamModel, TeamView, strSectionTemplate, strGameTemplate) {
    return {
      init: function() {
        this.model = new TournamentModel;
        this.model.bind('change', this.render, this);
        this.model.fetch();
        this.teamViews = {};
        this.emptyTeamViews = {};
        this.dropViews = [];
        this.sectionHB = handlebars.compile(strSectionTemplate);
        return this.gameHB = handlebars.compile(strGameTemplate);
      },
      render: function() {
        var elBracket, _ref;
        elBracket = $(this.sectionHB({
          "class": "regions"
        }));
        if ((_ref = this.model.get('regions')) != null) {
          _ref.forEach(__bind(function(region) {
            var elRegion, _ref2;
            elRegion = $(this.sectionHB({
              "class": "region region-" + region.regionId
            }));
            if ((_ref2 = region.rounds) != null) {
              _ref2.forEach(__bind(function(round) {
                var elRound, _ref3;
                elRound = $(this.sectionHB({
                  "class": "round round-" + round.roundId
                }));
                if ((_ref3 = round.games) != null) {
                  _ref3.forEach(__bind(function(game) {
                    var elGame, _ref4;
                    elGame = $(this.gameHB(game));
                    if ((_ref4 = game.teams) != null) {
                      _ref4.forEach(__bind(function(team) {
                        var teamView, teamZero;
                        team.regionId = region.regionId;
                        team.roundId = round.roundId;
                        team.gameId = game.gameId;
                        team.nextGame = (game.nextGame ? game.nextGame : null);
                        teamView = new TeamView({
                          model: new TeamModel(team)
                        });
                        teamView.on('drag', this.teamDrag, this);
                        teamView.on('drop', this.teamDrop, this);
                        this.teamViews["" + team.teamId] = teamView;
                        if (!(team.teamId && team.name)) {
                          this.emptyTeamViews["" + region.regionId + "-" + round.roundId + "-" + game.gameId + "-" + team.position] = teamView;
                        }
                        teamZero = elGame.find('.detail.team-0');
                        if (teamZero.val()) {
                          return teamView.$el.insertAfter(teamZero);
                        } else {
                          return teamView.$el.insertAfter(elGame.find('.detail.state'));
                        }
                      }, this));
                    }
                    return elRound.append(elGame);
                  }, this));
                }
                return elRegion.append(elRound);
              }, this));
            }
            return elBracket.append(elRegion);
          }, this));
        }
        return $('#bracketNode').append(elBracket);
      },
      teamDrag: function(model, ui) {
        return this.dropViews = this.recurNextGames(model, 'showDropZone');
      },
      teamDrop: function(model, ui) {
        var landingModel, _ref;
        if (this.dropViews.length > 0) {
          this.dropViews.forEach(function(view) {
            return view.hideDropZone();
          });
        } else {
          this.dropViews = this.recurNextGames(model, 'hideDropZone');
        }
        if (!model.get('teamId')) {
          landingModel = (_ref = this.teamViews["" + (ui.draggable.data('id'))]) != null ? _ref.model : void 0;
          return console.log("Team " + (landingModel.get('name')) + " has landed on game id " + (model.get('gameId')) + ".");
        }
      },
      recurNextGames: function(model, actionAttr, dropViews) {
        var nextGame, nextGameView, region;
        nextGame = model.get('nextGame');
        nextGameView = void 0;
        if (!dropViews) {
          dropViews = [];
        }
        region = _.find(this.model.get('regions'), __bind(function(region) {
          var round;
          return round = _.find(region.rounds, __bind(function(round) {
            var game;
            return game = _.find(round.games, __bind(function(game) {
              if (game.gameId === nextGame.gameId && region.regionId === nextGame.regionId) {
                return nextGameView = this.emptyTeamViews["" + region.regionId + "-" + round.roundId + "-" + game.gameId + "-" + nextGame.position];
              }
            }, this)) != null;
          }, this)) != null;
        }, this));
        if (nextGameView != null) {
          if (typeof nextGameView[actionAttr] === "function") {
            nextGameView[actionAttr]();
          }
        }
        if (nextGameView) {
          dropViews.push(nextGameView);
        }
        if (nextGameView && nextGameView.model.get('nextGame')) {
          return this.recurNextGames(nextGameView.model, actionAttr, dropViews);
        } else {
          return dropViews;
        }
      }
    };
  });
}).call(this);
