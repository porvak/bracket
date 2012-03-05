(function() {
  var __bind = function(fn, me){ return function(){ return fn.apply(me, arguments); }; };
  define(['lib/backbone', 'lib/jquery', 'lib/handlebars', 'text!html/gameTemplate.html'], function(Backbone, $, handlebars, strGameTemplate) {
    return Backbone.View.extend({
      initialize: function(options) {
        this.model.on('change', this.render, this);
        this.gameHB = handlebars.compile(strGameTemplate);
        return this.render();
      },
      render: function() {
        this.$el.html(this.gameHB(this.model.toJSON()));
        return this.$el.find('.team').each(__bind(function(index, team) {
          $(team).draggable({
            helper: 'clone',
            opacity: 0.6,
            start: __bind(function(event, ui) {
              return this.trigger('drag', this.model);
            }, this),
            stop: __bind(function(event, ui) {
              return this.trigger('drop', this.model);
            }, this)
          });
          return $(team).droppable({
            drop: this.drop,
            tolerance: 'pointer',
            over: function(event, ui) {},
            out: function(event, ui) {}
          });
        }, this));
      },
      events: {
        "click .game .details .detail": "click"
      },
      click: function(e) {
        var team;
        team = this.getTeam(e);
        return console.log(team.name);
      },
      drop: function(event, ui) {
        return $(this).attr('style', 'background-color:red');
      },
      getTeam: function(e) {
        var seatNum, seatStr, _ref, _ref2;
        seatStr = e != null ? (_ref = e.currentTarget) != null ? _ref.getAttribute('position') : void 0 : void 0;
        seatNum = parseInt(seatStr);
        return (_ref2 = this.model.get('teams')) != null ? _ref2[seatNum] : void 0;
      },
      showDropZone: function() {
        this.$el.addClass("highlight-game-drop");
        return console.log(this.model.get('gameId'));
      },
      hideDropZone: function() {
        this.$el.removeClass("highlight-game-drop");
        return console.log(this.model.get('gameId'));
      }
    });
  });
}).call(this);
