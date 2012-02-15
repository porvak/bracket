(function() {
  define(['lib/backbone', 'lib/jquery', 'text!html/bracketTemplate.html'], function(Backbone, $, strBracketTemplate) {
    return Backbone.View.extend({
      initialize: function(options) {
        this.container = $("#bracketNode");
        this.model.bind('change', this.render, this);
        return this.bracketTemplate = Handlebars.compile(strBracketTemplate);
      },
      render: function() {
        $(this.el).html(this.bracketTemplate(this.model.toJSON()));
        return this.container.append($(this.el));
      }
    });
  });
}).call(this);
