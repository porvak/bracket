(function() {

  define(['lib/jquery', 'lib/underscore', 'lib/handlebars', 'app/model/TournamentModel', 'app/model/TeamModel', 'app/view/TeamView', 'app/model/ScoreModel', 'app/view/ScoreView', 'text!html/sectionTemplate.html', 'text!html/gameTemplate.html', 'text!html/scoreTemplate.html', 'text!html/browserDetectTemplate.html'], function($, _, handlebars, TournamentModel, TeamModel, TeamView, ScoreModel, ScoreView, strSectionTemplate, strGameTemplate, strScoreTemplate, strBrowserDetectTemplate) {
    return {
      init: function() {
        this.model = new TournamentModel;
        this.model.bind('change', this.render, this);
        this.model.fetch();
        this.teamViews = {};
        this.dropViews = [];
        this.sectionHB = handlebars.compile(strSectionTemplate);
        this.gameHB = handlebars.compile(strGameTemplate);
        this.browserDetectHB = handlebars.compile(strBrowserDetectTemplate);
        if ($.browser.msie) {
          $(this.browserDetectHB({})).dialog({
            title: 'The Bracket App',
            height: 200,
            width: 600,
            position: ['center', 50],
            modal: true,
            resizable: false
          });
          return $('#ie-detect').focus();
        }
      },
      render: function() {
        var elBracket, scoreView, _ref,
          _this = this;
        if (this.model.get('pickStatus') !== "OPEN") {
          $('.navbar.leaderboard').removeClass('hidden');
          $('#scoreboard').removeClass('hidden');
        }
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
      advanceTeam: function(landingView) {
        var baseView, nextGame, pendingSaveArr, _ref, _ref2, _ref3, _ref4;
        if (!((landingView != null ? landingView.model.get('teamId') : void 0) || (landingView != null ? (_ref = landingView.model.get('userPick')) != null ? _ref.teamId : void 0 : void 0))) {
          return;
        }
        nextGame = landingView != null ? landingView.model.get('nextGame') : void 0;
        baseView = this.teamViews["" + nextGame.regionId + "-" + nextGame.gameId + "-" + nextGame.position];
        pendingSaveArr = [];
        if (landingView && baseView) {
          pendingSaveArr.push({
            view: baseView,
            model: {
              userPick: {
                name: landingView.model.get('name') || ((_ref2 = landingView.model.get('userPick')) != null ? _ref2.name : void 0),
                teamId: landingView.model.get('teamId') || ((_ref3 = landingView.model.get('userPick')) != null ? _ref3.teamId : void 0),
                seed: landingView.model.get('seed') || ((_ref4 = landingView.model.get('userPick')) != null ? _ref4.seed : void 0),
                regionId: landingView.model.get('regionId')
              },
              previousGame: {
                regionId: landingView.model.get('regionId'),
                gameId: landingView.model.get('gameId'),
                position: landingView.model.get('position')
              }
            }
          });
          return this.chainSaveCallbacks(pendingSaveArr, this.checkRemoveFutureWins, [baseView, baseView.model.get('previousGame')]);
        }
      },
      removeTeam: function(startingView) {
        var deleteCallbackArr, nextTeamViewArr, teamId, _ref;
        teamId = startingView != null ? (_ref = startingView.model.get('userPick')) != null ? _ref.teamId : void 0 : void 0;
        if (!teamId) return;
        deleteCallbackArr = [startingView];
        nextTeamViewArr = this.recurNextTeamViews(startingView);
        nextTeamViewArr.forEach(function(view) {
          var _ref2;
          if (((_ref2 = view.model.get('userPick')) != null ? _ref2.teamId : void 0) === teamId) {
            return deleteCallbackArr.push(view);
          }
        });
        return this.chainDeleteCallbacks(deleteCallbackArr);
      },
      teamDrop: function(baseView, ui, landingView) {
        var lastLandingView, pendingSaveArr, postError, previousGameView,
          _this = this;
        postError = false;
        pendingSaveArr = [];
        if (ui) landingView = this.teamViews["" + (ui.draggable.data('locator'))];
        if (this.validDropZone(baseView, landingView)) {
          previousGameView = landingView;
          lastLandingView = _.find(this.dropViews, function(dropView, i) {
            var eachView, _ref, _ref2, _ref3;
            eachView = _this.dropViews[i];
            pendingSaveArr.push({
              view: eachView,
              model: {
                userPick: {
                  name: landingView.model.get('name') || ((_ref = landingView.model.get('userPick')) != null ? _ref.name : void 0),
                  teamId: landingView.model.get('teamId') || ((_ref2 = landingView.model.get('userPick')) != null ? _ref2.teamId : void 0),
                  seed: landingView.model.get('seed') || ((_ref3 = landingView.model.get('userPick')) != null ? _ref3.seed : void 0),
                  regionId: landingView.model.get('regionId')
                },
                previousGame: {
                  regionId: previousGameView.model.get('regionId'),
                  gameId: previousGameView.model.get('gameId'),
                  position: previousGameView.model.get('position')
                }
              }
            });
            previousGameView = eachView;
            return _.isEqual(baseView, dropView);
          });
          return this.chainSaveCallbacks(pendingSaveArr, this.checkRemoveFutureWins, [lastLandingView, baseView.model.get('previousGame')]);
        }
      },
      chainSaveCallbacks: function(pendingSaveArr, callback, callbackArgs) {
        var view, _ref, _ref2,
          _this = this;
        view = (_ref = _.first(pendingSaveArr)) != null ? _ref.view : void 0;
        if (view != null) view.$el.addClass('saving');
        return view != null ? view.model.save((_ref2 = _.first(pendingSaveArr)) != null ? _ref2.model : void 0, {
          wait: true,
          success: function(model, response) {
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
            if (response.status === 404) {
              return alert('Please sign in using twitter.');
            }
          }
        }) : void 0;
      },
      chainDeleteCallbacks: function(pendingDeleteArr) {
        var view,
          _this = this;
        view = _.first(pendingDeleteArr);
        if (view != null) view.$el.addClass('saving');
        return view != null ? view.model.deletePick({
          success: function() {
            view.$el.removeClass('saving');
            view.model.set({
              userPick: null,
              previousGame: null
            });
            if (pendingDeleteArr.length > 1) {
              return _this.chainDeleteCallbacks(_.last(pendingDeleteArr, pendingDeleteArr.length - 1));
            }
          },
          error: function() {
            return pendingDeleteArr.forEach(function(view) {
              return view.$el.removeClass('saving');
            });
          }
        }) : void 0;
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
      recurPreviousTeamViews: function(previousGame, actionAttr, actionAttrArgs, previousTeamViewArr) {
        var previousTeamView, _ref;
        previousTeamView = null;
        previousTeamViewArr = previousTeamViewArr || [];
        if (previousGame) {
          previousTeamView = this.teamViews["" + previousGame.regionId + "-" + previousGame.gameId + "-" + previousGame.position];
        }
        if (previousTeamView) {
          if ((_ref = previousTeamView[actionAttr]) != null) {
            _ref.apply(previousTeamView, actionAttrArgs);
          }
          previousTeamViewArr.push(previousTeamView);
          return this.recurPreviousTeamViews(previousTeamView.model.get('previousGame'), actionAttr, actionAttrArgs, previousTeamViewArr);
        } else {
          return previousTeamViewArr;
        }
      },
      checkRemoveFutureWins: function(baseView, previousGame) {
        var firstView, nextViewArr, pendingDeleteArr, prevTeamId, previousViewArr, _ref;
        nextViewArr = this.recurNextTeamViews(baseView);
        if (nextViewArr.length === 0) return;
        pendingDeleteArr = [];
        previousViewArr = this.recurPreviousTeamViews(previousGame);
        if (previousViewArr.length > 0) {
          firstView = _.first(previousViewArr);
          prevTeamId = ((_ref = firstView.model.get('userPick')) != null ? _ref.teamId : void 0) || firstView.model.get('teamId');
          nextViewArr.forEach(function(view) {
            var _ref2;
            if (((_ref2 = view.model.get('userPick')) != null ? _ref2.teamId : void 0) === prevTeamId || view.model.get('teamId') === prevTeamId) {
              return pendingDeleteArr.push(view);
            }
          });
          if (pendingDeleteArr.length > 0) {
            return this.chainDeleteCallbacks(pendingDeleteArr);
          }
        }
      },
      validDropZone: function(baseView, landingView) {
        if (!landingView) return false;
        if (this.dropViews.length === 0) {
          this.dropViews = this.recurNextTeamViews(landingView);
        }
        return _.find(this.dropViews, function(dropView) {
          return _.isEqual(baseView, dropView);
        });
      }
    };
  });

}).call(this);
