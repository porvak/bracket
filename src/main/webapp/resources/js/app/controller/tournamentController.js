(function() {
  var __bind = function(fn, me){ return function(){ return fn.apply(me, arguments); }; };
  define(['lib/jquery', 'lib/handlebars', 'app/model/TournamentModel', 'app/model/GameModel', 'app/view/GameView', 'text!html/sectionTemplate.html'], function($, handlebars, TournamentModel, GameModel, GameView, strSectionTemplate) {
    return {
      init: function() {
        this.model = new TournamentModel;
        this.model.bind('change', this.render, this);
        this.model.fetch();
        this.gameViews = {};
        return this.sectionHB = handlebars.compile(strSectionTemplate);
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
                    var gameModel, gameView;
                    game.regionId = region.regionId;
                    game.roundId = round.roundId;
                    gameModel = new GameModel(game);
                    gameView = new GameView({
                      model: gameModel
                    });
                    gameView.on('drag', this.showDropZones, this);
                    gameView.on('drop', this.hideDropZones, this);
                    this.gameViews["" + region.regionId + "-" + round.roundId + "-" + game.gameId] = gameView;
                    return elRound.append(gameView.el);
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
      showDropZones: function(model) {
        var nextGame, nextGameView, region;
        nextGame = model.get('nextGame');
        nextGameView = void 0;
        region = _.find(this.model.get('regions'), __bind(function(region) {
          var round;
          return round = _.find(region.rounds, __bind(function(round) {
            var game;
            return game = _.find(round.games, __bind(function(game) {
              if (game.gameId === nextGame.gameId && region.regionId === nextGame.regionId) {
                return nextGameView = this.gameViews["" + region.regionId + "-" + round.roundId + "-" + game.gameId];
              }
            }, this)) != null;
          }, this)) != null;
        }, this));
        if (nextGameView != null) {
          nextGameView.highlight();
        }
        if (nextGameView && nextGameView.model.get('nextGame')) {
          return this.showDropZones(nextGameView.model);
        } else {
          return null;
        }
      },
      hideDropZones: function(index, team, model) {
        console.log('showDropZones');
        console.log(index);
        console.log(team);
        return console.log(model);
      }
    };
  });
}).call(this);
