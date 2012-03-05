(function() {
  var __bind = function(fn, me){ return function(){ return fn.apply(me, arguments); }; };
  define(['lib/backbone', 'lib/jquery', 'lib/handlebars', 'text!html/teamTemplate.html'], function(Backbone, $, handlebars, strTeamTemplate) {
    return Backbone.View.extend({
      initialize: function(options) {
        this.model.on('change', this.render, this);
        this.teamHB = handlebars.compile(strTeamTemplate);
        return this.render();
      },
      render: function() {
        var teamDiv;
        this.$el = $(this.teamHB(this.model.toJSON()));
        teamDiv = $(this.$el.find('.team'));
        teamDiv.draggable({
          helper: 'clone',
          opacity: 0.6,
          start: __bind(function(event, ui) {
            return this.trigger('drag', this.model);
          }, this),
          stop: __bind(function(event, ui) {
            return this.trigger('drop', this.model);
          }, this)
        });
        return teamDiv.droppable({
          drop: this.drop,
          tolerance: 'pointer',
          over: function(event, ui) {},
          out: function(event, ui) {}
        });
      },
      events: {
        "click .detail": "click"
      },
      click: function(e) {
        return console.log(this.model.get('name'));
      },
      drop: function(event, ui) {
        return $(this).attr('style', 'background-color:red');
      },
      showDropZone: function() {
        this.$el.addClass("highlight-game-drop");
        return console.log("" + (this.model.get('gameId')));
      },
      hideDropZone: function() {
        this.$el.removeClass("highlight-game-drop");
        return console.log("" + (this.model.get('gameId')));
      }
    });
  });
}).call(this);
