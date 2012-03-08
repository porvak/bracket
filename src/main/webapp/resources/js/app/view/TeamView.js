(function() {

  define(['lib/backbone', 'lib/jquery', 'lib/handlebars', 'text!html/teamTemplate.html'], function(Backbone, $, handlebars, strTeamTemplate) {
    return Backbone.View.extend({
      initialize: function(options) {
        this.model.on('change', this.render, this);
        this.teamHB = handlebars.compile(strTeamTemplate);
        return this.render();
      },
      render: function() {
        var displayTeam, userPick;
        if (!this.model.get('teamId')) {
          userPick = this.model.get('userPick');
          if (userPick && userPick.id) {
            displayTeam = userPick;
            displayTeam.teamId = userPick.id;
          } else {
            displayTeam = this.model.toJSON();
          }
        } else {
          displayTeam = this.model.toJSON();
        }
        if (this.$el.html()) {
          this.$el.html($(this.teamHB(displayTeam)).html());
        } else {
          this.$el = $(this.teamHB(displayTeam));
        }
        if (this.model.get('pickable')) this.$el.addClass('pickable');
        if (this.model.get('teamId')) this.setupDrag();
        this.setupDrop();
        return this.checkWrongPick();
      },
      checkWrongPick: function() {
        var _ref;
        if (this.model.get('userPick') && this.model.get('teamId') && this.model.get('teamId') !== ((_ref = this.model.get('userPick')) != null ? _ref.id : void 0)) {
          return this.$el.find('.team').addClass('lineThrough');
        }
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
        "click .pick_icon": "triggerAdvance",
        "mouseenter .team": "showDelete",
        "mouseleave .team": "hideDelete",
        "click .team-delete": "triggerDelete"
      },
      triggerAdvance: function(e) {
        return this.trigger('advance', this);
      },
      triggerDelete: function(e) {
        return this.trigger('remove', this);
      },
      showDropZone: function() {
        return this.$el.addClass("highlight-team-drop");
      },
      hideDropZone: function() {
        return this.$el.removeClass("highlight-team-drop");
      },
      showDelete: function() {
        if (this.model.get('roundId') !== 1 || this.model.get('regionId') === 5) {
          return this.$el.find('.team-delete').removeClass("hidden");
        }
      },
      hideDelete: function() {
        if (this.model.get('roundId') !== 1 || this.model.get('regionId') === 5) {
          return this.$el.find('.team-delete').addClass("hidden");
        }
      }
    });
  });

}).call(this);
