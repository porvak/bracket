(function() {
  var __bind = function(fn, me){ return function(){ return fn.apply(me, arguments); }; };
  define(['lib/jquery', 'lib/handlebars', 'app/model/TournamentModel', 'app/model/GameModel', 'app/view/GameView', 'text!html/sectionTemplate.html'], function($, handlebars, TournamentModel, GameModel, GameView, strSectionTemplate) {
    return {
      init: function() {
        this.model = new TournamentModel;
        this.gameViewArr = [];
        this.model.bind('change', this.render, this);
        this.model.fetch();
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
              "class": "region region-" + region.id
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
                    game.regionId = region.id;
                    gameModel = new GameModel(game);
                    gameView = new GameView({
                      model: gameModel
                    });
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
      }
    };
  });
}).call(this);
