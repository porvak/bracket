(function() {

  define(['lib/backbone', 'lib/jquery', 'lib/handlebars', 'text!html/teamTemplate.html'], function(Backbone, $, handlebars, strTeamTemplate) {
    return Backbone.View.extend({
      initialize: function(options) {
        this.model.on('change', this.render, this);
        this.teamHB = handlebars.compile(strTeamTemplate);
        return this.render();
      },
      render: function() {
        var teamDiv,
          _this = this;
        this.$el = $(this.teamHB(this.model.toJSON()));
        teamDiv = $(this.$el.find('.team'));
        teamDiv.draggable({
          helper: 'clone',
          opacity: 0.6,
          start: function(event, ui) {
            return _this.trigger('drag', _this.model);
          },
          stop: function(event, ui) {
            return _this.trigger('drop', _this.model);
          }
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
