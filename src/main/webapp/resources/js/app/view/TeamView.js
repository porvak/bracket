(function() {

  define(['lib/backbone', 'lib/jquery', 'lib/handlebars', 'app/controller/tournamentController', 'text!html/teamTemplate.html'], function(Backbone, $, handlebars, tournamentController, strTeamTemplate) {
    return Backbone.View.extend({
      initialize: function(options) {
        this.model.on('change', this.render, this);
        this.teamHB = handlebars.compile(strTeamTemplate);
        return this.render();
      },
      render: function() {
        var _this = this;
        this.$el = $(this.teamHB(this.model.toJSON()));
        $(this.$el.find('.team')).draggable({
          helper: 'clone',
          opacity: 0.6,
          start: function(event, ui) {
            return _this.trigger('drag', _this, ui);
          }
        });
        return this.$el.droppable({
          tolerance: 'pointer',
          drop: function(event, ui) {
            return _this.trigger('drop', _this, ui);
          },
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
