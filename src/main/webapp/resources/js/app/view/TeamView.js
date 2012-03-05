(function() {
  var __bind = function(fn, me){ return function(){ return fn.apply(me, arguments); }; };
  define(['lib/backbone', 'lib/jquery', 'lib/handlebars', 'app/controller/tournamentController', 'text!html/teamTemplate.html'], function(Backbone, $, handlebars, tournamentController, strTeamTemplate) {
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
            return this.trigger('drag', this.model, ui);
          }, this)
        });
        return teamDiv.droppable({
          tolerance: 'pointer',
          drop: __bind(function(event, ui) {
            return this.trigger('drop', this.model, ui);
          }, this),
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
      showDropZone: function() {
        return this.$el.addClass("highlight-game-drop");
      },
      hideDropZone: function() {
        return this.$el.removeClass("highlight-game-drop");
      },
      isValidDrop: function() {}
    });
  });
}).call(this);
