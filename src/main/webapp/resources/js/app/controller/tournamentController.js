(function() {

  define(['lib/jquery', 'lib/underscore', 'lib/handlebars', 'app/model/TournamentModel', 'app/model/TeamModel', 'app/view/TeamView', 'app/model/ScoreModel', 'app/view/ScoreView', 'text!html/sectionTemplate.html', 'text!html/gameTemplate.html', 'text!html/scoreTemplate.html'], function($, _, handlebars, TournamentModel, TeamModel, TeamView, ScoreModel, ScoreView, strSectionTemplate, strGameTemplate, strScoreTemplate) {
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
        var elBracket, scoreView, _ref,
          _this = this;
        console.log("GET: http://" + (window.location.host + this.model.url()) + "\n\n");
        scoreView = new ScoreView({
          model: new ScoreModel({
            tieBreaker: this.model.get('tieBreaker')
          })
        });
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
                  "class": "round round-" + round.roundId,
                  finalScoreSection: region.regionId === 5 && round.roundId === 3 ? true : void 0
                }));
                if ((_ref3 = round.games) != null) {
                  _ref3.forEach(function(game) {
                    var elGame, _ref4;
                    if (region.regionId === 5 && round.roundId === 2 && game.gameId === 3) {
                      game.finalVS = true;
                    }
                    elGame = $(_this.gameHB(game));
                    if ((_ref4 = game.teams) != null) {
                      _ref4.forEach(function(team) {
                        var finalVS, teamView, teamZero;
                        team.regionId = region.regionId;
                        team.roundId = round.roundId;
                        team.gameId = game.gameId;
                        team.nextGame = (game.nextGame ? game.nextGame : null);
                        if (team.teamId) team.pickable = true;
                        team.locator = "" + region.regionId + "-" + game.gameId + "-" + team.position;
                        teamView = new TeamView({
                          model: new TeamModel(team)
                        });
                        teamView.on('drag', _this.findShowDropViews, _this);
                        teamView.on('dragStop', _this.hideDropZones, _this);
                        teamView.on('drop', _this.teamDrop, _this);
                        teamView.on('advance', _this.advanceTeam, _this);
                        teamView.on('remove', _this.removeTeam, _this);
                        _this.teamViews[team.locator] = teamView;
                        teamZero = elGame.find('.detail.team-0');
                        finalVS = elGame.find('.detail.finalVS');
                        if (teamZero.html()) {
                          if (finalVS.html()) {
                            return teamView.$el.insertAfter(finalVS);
                          } else {
                            return teamView.$el.insertAfter(teamZero);
                          }
                        } else {
                          return teamView.$el.insertAfter(elGame.find('.detail.state'));
                        }
                      });
                    }
                    elRound.append(elGame);
                    if (region.regionId === 5 && round.roundId === 3) {
                      return elRound.append(scoreView.$el);
                    }
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
      advanceTeam: function(startingView) {
        var endingView, nextGame;
        if (!(startingView != null ? startingView.model.get('teamId') : void 0)) {
          return;
        }
        nextGame = startingView != null ? startingView.model.get('nextGame') : void 0;
        endingView = this.teamViews["" + nextGame.regionId + "-" + nextGame.gameId + "-" + nextGame.position];
        if (startingView && endingView) {
          this.chainSaveCallbacks([
            {
              view: endingView,
              model: {
                name: startingView.model.get('name'),
                teamId: startingView.model.get('teamId'),
                seed: startingView.model.get('seed')
              }
            }
          ]);
          return this.checkRemoveFutureWins(endingView);
        }
      },
      removeTeam: function(startingView) {
        if (!(startingView != null ? startingView.model.get('teamId') : void 0)) {
          return;
        }
        this.checkRemoveFutureWins(startingView);
        return startingView.model.set({
          name: null,
          teamId: null,
          seed: null
        });
      },
      teamDrop: function(baseView, ui) {
        var landingView, lastLandingView, pendingSaveArr, postError,
          _this = this;
        postError = false;
        pendingSaveArr = [];
        landingView = this.teamViews["" + (ui.draggable.data('locator'))];
        if (this.validDropZone(baseView, landingView)) {
          lastLandingView = _.find(this.dropViews, function(dropView, i) {
            var eachView;
            eachView = _this.dropViews[i];
            pendingSaveArr.push({
              view: eachView,
              model: {
                name: landingView.model.get('name'),
                teamId: landingView.model.get('teamId'),
                seed: landingView.model.get('seed')
              }
            });
            return _.isEqual(baseView, dropView);
          });
        }
        return this.chainSaveCallbacks(pendingSaveArr, this.checkRemoveFutureWins, [lastLandingView]);
      },
      chainSaveCallbacks: function(pendingSaveArr, callback, callbackArgs) {
        var view, _ref, _ref2,
          _this = this;
        view = (_ref = _.first(pendingSaveArr)) != null ? _ref.view : void 0;
        if (view != null) view.$el.addClass('saving');
        return view != null ? view.model.save((_ref2 = _.first(pendingSaveArr)) != null ? _ref2.model : void 0, {
          wait: true,
          success: function(model, response) {
            console.log("POST: http://" + (window.location.host + model.url()) + "\nJSON:" + (JSON.stringify(model.toJSON())) + "\n\n");
            view.$el.removeClass('saving');
            if (pendingSaveArr.length > 1) {
              return _this.chainSaveCallbacks(_.last(pendingSaveArr, pendingSaveArr.length - 1), callback, callbackArgs);
            } else {
              return callback != null ? callback.apply(_this, callbackArgs) : void 0;
            }
          },
          error: function(model, response) {
            pendingSaveArr.forEach(function(viewObj) {
              return viewObj.view.$el.removeClass('saving');
            });
            if (response.status === 404) alert('Please sign in using twitter.');
            if (response.status === 500) {
              return console.log("POST ERROR: http://" + (window.location.host + model.url()) + "\nJSON:" + (JSON.stringify(model.toJSON())) + "\n\n");
            }
          }
        }) : void 0;
      },
      chainDeleteCallbacks: function(pendingDeleteArr, callback, callbackArgs) {
        var view, _ref;
        view = (_ref = _.first(pendingDeleteArr)) != null ? _ref.view : void 0;
        return pendingDeleteArr.forEach(function(userPick) {
          return userPick.view.model.set(userPick.model);
        });
      },
      recurNextTeamViews: function(baseView, actionAttr, actionAttrArgs, nextTeamViewArr) {
        var nextGame, nextTeamView, _ref;
        nextTeamView = null;
        nextTeamViewArr = nextTeamViewArr || [];
        nextGame = baseView != null ? baseView.model.get('nextGame') : void 0;
        if (nextGame) {
          nextTeamView = this.teamViews["" + nextGame.regionId + "-" + nextGame.gameId + "-" + nextGame.position];
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
        var previousGame, previousTeamView, _ref;
        previousTeamView = null;
        previousTeamViewArr = previousTeamViewArr || [];
        previousGame = baseView.model.get('previousGame');
        if (previousGame) {
          previousTeamView = this.teamViews["" + previousGame.regionId + "-" + previousGame.gameId + "-" + previousGame.position];
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
      },
      checkRemoveFutureWins: function(baseView) {
        var nextViewArr, pendingSaveArr, prevTeamId, previousViewArr;
        nextViewArr = this.recurNextTeamViews(baseView);
        if (nextViewArr.length === 0) return;
        pendingSaveArr = [];
        previousViewArr = this.recurPreviousTeamViews(baseView);
        if (previousViewArr.length > 0) {
          prevTeamId = _.first(previousViewArr).model.get('teamId');
          nextViewArr.forEach(function(view) {
            if (view.model.get('teamId') === prevTeamId) {
              return pendingSaveArr.push({
                view: view,
                model: {
                  name: null,
                  teamId: null,
                  seed: null
                }
              });
            }
          });
          if (pendingSaveArr.length > 0) {
            return this.chainDeleteCallbacks(pendingSaveArr);
          }
        }
      },
      validDropZone: function(baseView, landingView) {
        if (!landingView) return false;
        return _.find(this.dropViews, function(dropView) {
          return _.isEqual(baseView, dropView);
        });
      }
    };
  });

}).call(this);
