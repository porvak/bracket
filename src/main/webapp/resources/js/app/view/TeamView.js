(function() {

  define(['lib/backbone', 'lib/jquery', 'lib/handlebars', 'app/controller/tournamentController', 'text!html/teamTemplate.html'], function(Backbone, $, handlebars, tournamentController, strTeamTemplate) {
    return Backbone.View.extend({
      initialize: function(options) {
        this.model.on('change', this.update, this);
        this.teamHB = handlebars.compile(strTeamTemplate);
        return this.render();
      },
      render: function() {
        this.$el = $(this.teamHB(this.model.toJSON()));
        if (this.model.get('pickable')) this.$el.addClass('pickable');
        if (this.model.get('teamId')) this.setupDrag();
        return this.setupDrop();
      },
      update: function() {
        this.$el.html($(this.teamHB(this.model.toJSON())).html());
        if (this.model.get('teamId')) return this.setupDrag();
      },
      setupDrag: function() {
        var _this = this;
        return this.$el.draggable({
          helper: 'clone',
          opacity: 0.6,
          zIndex: 2000,
          start: function(event, ui) {
            return _this.trigger('drag', _this, ui);
          },
          stop: function(event, ui) {
            return _this.trigger('dragStop', _this, ui);
          }
        });
      },
      setupDrop: function() {
        var _this = this;
        return this.$el.droppable({
          tolerance: 'pointer',
          drop: function(event, ui) {
            return _this.trigger('drop', _this, ui);
          }
        });
      },
      events: {
        "click .detail": "click"
      },
      click: function(e) {
        return console.log(this.model.get('name'));
      },
      showDropZone: function() {
        return this.$el.addClass("highlight-team-drop");
      },
      hideDropZone: function() {
        return this.$el.removeClass("highlight-team-drop");
      },
      reset: function() {
        return this.model.set({
          name: null,
          teamId: null,
          seed: null
        });
      }
    });
  });

}).call(this);
