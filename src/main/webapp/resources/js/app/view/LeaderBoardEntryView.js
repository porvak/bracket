(function() {

  define(['lib/backbone', 'lib/jquery', 'text!html/leaderBoardEntryTemplate.html'], function(Backbone, $, strLeaderBoardEntryTemplate) {
    return Backbone.View.extend({
      tagName: 'tr',
      events: {
        mouseenter: "showHover",
        mouseleave: "hideHover"
      },
      initialize: function(options) {
        this.model = this.options.model;
        return this.entryHB = Handlebars.compile(strLeaderBoardEntryTemplate);
      },
      render: function() {
        this.$el.html(this.entryHB(this.model.toJSON()));
        return this;
      },
      showHover: function(event) {
        return $(this.el).addClass("highlight");
      },
      hideHover: function(event) {
        return $(this.el).removeClass("highlight");
      }
    });
  });

}).call(this);
