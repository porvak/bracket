(function() {
  var __bind = function(fn, me){ return function(){ return fn.apply(me, arguments); }; };
  define(['lib/backbone', 'lib/jquery', 'lib/handlebars', 'app/controller/tournamentController', 'text!html/teamTemplate.html'], function(Backbone, $, handlebars, tournamentController, strTeamTemplate) {
    return Backbone.View.extend({
      initialize: function(options) {
        this.model.on('change', this.update, this);
        this.teamHB = handlebars.compile(strTeamTemplate);
        return this.render();
      },
      render: function() {
        this.$el = $(this.teamHB(this.model.toJSON()));
        $(this.$el.find('.team')).draggable({
          helper: 'clone',
          opacity: 0.6,
          start: __bind(function(event, ui) {
            return this.trigger('drag', this, ui);
          }, this),
          stop: __bind(function(event, ui) {
            return this.trigger('dragStop', this, ui);
          }, this)
        });
        return this.$el.droppable({
          tolerance: 'pointer',
          drop: __bind(function(event, ui) {
            return this.trigger('drop', this, ui);
          }, this),
          over: function(event, ui) {},
          out: function(event, ui) {}
        });
      },
      update: function() {
        return this.$el.replaceWith(this.teamHB(this.model.toJSON()));
      },
      events: {
        "click .detail": "click"
      },
      click: function(e) {
        return console.log(this.model.get('name'));
      },
      showDropZone: function() {
        return this.$el.addClass("highlight-game-drop");
      },
      hideDropZone: function() {
        return this.$el.removeClass("highlight-game-drop");
      }
    });
  });
}).call(this);
