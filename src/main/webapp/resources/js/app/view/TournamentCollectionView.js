(function() {
  define(['lib/backbone', 'lib/jquery', 'app/view/GameView'], function(Backbone, $, GameView) {
    return Backbone.View.extend({
      initialize: function(options) {
        return this.collection.each(function(game) {
          var view;
          view = new GameView({
            model: game
          });
          return view.render();
        });
      }
    });
  });
}).call(this);
