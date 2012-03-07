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
                        teamView.on('drag', _this.findShowDropViews, _this);
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
      findShowDropViews: function(baseView) {
        return this.dropViews = this.recurNextTeamViews(baseView, 'showDropZone');
      },
      hideDropZones: function(baseView) {
        if (!this.dropViews) {
          return this.dropViews = this.recurNextTeamViews(baseView, 'hideDropZone');
        } else {
          return this.dropViews.forEach(function(view) {
            return view.hideDropZone();
          });
        }
      },
      teamDrop: function(baseView, ui) {
        var landingView, lastLandingView, pendingViews, postError,
          _this = this;
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
        var baseView, landingView, _ref, _ref2,
          _this = this;
        baseView = (_ref = _.first(pendingViews)) != null ? _ref.baseView : void 0;
        landingView = (_ref2 = _.first(pendingViews)) != null ? _ref2.landingView : void 0;
        return baseView != null ? baseView.model.save({
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
        }) : void 0;
      },
      validDropZone: function(baseView, landingView) {
        if (!landingView) return false;
        return _.find(this.dropViews, function(dropView) {
          return _.isEqual(baseView, dropView);
        });
      },
      checkBrokenLinks: function(baseView) {
        var nextViewArr, previousViewArr;
        nextViewArr = this.recurNextTeamViews(baseView);
        if (nextViewArr.length === 0) return;
        previousViewArr = this.recurPreviousTeamViews(baseView);
        if (previousViewArr.length > 0) {
          return this.recurNextTeamViews(baseView, 'brokenLink', [baseView.model.get('teamId')]);
        }
      },
      recurNextTeamViews: function(baseView, actionAttr, actionAttrArgs, nextTeamViewArr) {
        var nextGame, nextTeamView, _ref,
          _this = this;
        nextTeamView = null;
        nextTeamViewArr = nextTeamViewArr || [];
        nextGame = baseView != null ? baseView.model.get('nextGame') : void 0;
        if (nextGame) {
          _.find(this.model.get('regions'), function(region) {
            return _.find(region.rounds, function(round) {
              return _.find(round.games, function(game) {
                if (game.gameId === nextGame.gameId && region.regionId === nextGame.regionId) {
                  return nextTeamView = _this.teamViews["" + region.regionId + "-" + round.roundId + "-" + game.gameId + "-" + nextGame.position];
                }
              });
            });
          });
        }
        if (nextTeamView) {
          nextTeamViewArr.push(nextTeamView);
          if ((_ref = nextTeamView[actionAttr]) != null) {
            _ref.apply(nextTeamView, actionAttrArgs);
          }
        }
        if (nextTeamView != null ? nextTeamView.model.get('nextGame') : void 0) {
          return this.recurNextTeamViews(nextTeamView, actionAttr, actionAttrArgs, nextTeamViewArr);
        } else {
          return nextTeamViewArr;
        }
      },
      recurPreviousTeamViews: function(baseView, actionAttr, actionAttrArgs, previousTeamViewArr) {
        var gameId, previousTeamView, teamId, _ref,
          _this = this;
        previousTeamView = null;
        previousTeamViewArr = previousTeamViewArr || [];
        gameId = baseView.model.get('gameId');
        teamId = baseView.model.get('teamId');
        if (gameId && teamId) {
          _.find(this.model.get('regions'), function(region) {
            return _.find(region.rounds, function(round) {
              return _.find(round.games, function(game) {
                var _ref;
                if (gameId === ((_ref = game.nextGame) != null ? _ref.gameId : void 0)) {
                  return _.find(game.teams, function(team) {
                    if (team.teamId === teamId) {
                      return previousTeamView = _this.teamViews["" + region.regionId + "-" + round.roundId + "-" + game.gameId + "-" + team.position];
                    }
                  });
                }
              });
            });
          });
        }
        if (previousTeamView) {
          if ((_ref = previousTeamView[actionAttr]) != null) {
            _ref.apply(previousTeamView, actionAttrArgs);
          }
          previousTeamViewArr.push(previousTeamView);
          return this.recurPreviousTeamViews(previousTeamView, actionAttr, actionAttrArgs, previousTeamViewArr);
        } else {
          return previousTeamViewArr;
        }
      }
    };
  });

}).call(this);
