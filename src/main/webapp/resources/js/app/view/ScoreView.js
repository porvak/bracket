(function() {

  define(['lib/backbone', 'lib/jquery', 'lib/handlebars', 'text!html/scoreTemplate.html'], function(Backbone, $, handlebars, strScoreTemplate) {
    return Backbone.View.extend({
      initialize: function(options) {
        this.model.on('change', this.update, this);
        this.scoreHB = handlebars.compile(strScoreTemplate);
        return this.render();
      },
      render: function() {
        this.$el = $(this.scoreHB(this.model.toJSON()));
        if (!this.model.get('tieBreaker')) {
          return this.enableEdit();
        } else {
          return this.disableEdit();
        }
      },
      update: function() {
        return this.$el.html($(this.scoreHB(this.model.toJSON())).html());
      },
      events: {
        "click .submit-score": "submitScore",
        "click .edit-score": "enableEdit",
        "blur #combined input": "submitScore",
        "click #combined input": "enableEdit"
      },
      submitScore: function() {
        var score,
          _this = this;
        score = parseInt(this.$el.find('input').val(), 10);
        if (score !== this.score) {
          if (score >= 0 && !this.saving) {
            this.$el.addClass('saving');
            this.saving = true;
            return this.model.save({
              tieBreaker: score
            }, {
              wait: true,
              success: function(model, response) {
                console.log("POST: http://" + (window.location.host + model.url()) + "\nJSON:" + (JSON.stringify(model.toJSON())) + "\n\n");
                _this.$el.removeClass('saving');
                _this.score = score;
                _this.saving = false;
                return _this.disableEdit();
              },
              error: function(model, response) {
                _this.$el.removeClass('saving');
                if (response.status === 404) {
                  alert('Please sign in using twitter.');
                }
                if (response.status === 500) {
                  return console.log("POST ERROR: http://" + (window.location.host + model.url()) + "\nJSON:" + (JSON.stringify(model.toJSON())) + "\n\n");
                }
              }
            });
          }
        } else {
          return this.disableEdit();
        }
      },
      enableEdit: function(e) {
        var input;
        this.$el.find('.submit-score').removeClass('hidden');
        this.$el.find('.edit-score').addClass('hidden');
        input = this.$el.find('input');
        input.removeAttr('disabled');
        input.removeClass('score-locked');
        input.addClass('score-unlocked');
        return input.focus();
      },
      disableEdit: function(e) {
        var input;
        input = this.$el.find('input');
        input.attr('disabled', 'disabled');
        input.addClass('score-locked');
        input.removeClass('score-unlocked');
        this.$el.find('.submit-score').addClass('hidden');
        return this.$el.find('.edit-score').removeClass('hidden');
      }
    });
  });

}).call(this);
