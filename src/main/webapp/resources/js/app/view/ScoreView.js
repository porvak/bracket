(function() {

  define(['lib/backbone', 'lib/jquery', 'lib/handlebars', 'text!html/scoreTemplate.html'], function(Backbone, $, handlebars, strScoreTemplate) {
    return Backbone.View.extend({
      initialize: function(options) {
        this.model.on('change', this.update, this);
        this.scoreHB = handlebars.compile(strScoreTemplate);
        return this.render();
      },
      render: function() {
        this.$el = $(this.scoreHB({}));
        if (!this.model.get('tieBreaker')) return this.editOn();
      },
      events: {
        "click input": "editOn",
        "blur input": "editOff"
      },
      editOn: function(e) {
        return console.log('edit on');
      },
      editOff: function(e) {
        return console.log('edit off');
      }
    });
  });

}).call(this);
