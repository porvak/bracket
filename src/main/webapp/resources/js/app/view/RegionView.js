(function() {
  define(['lib/backbone', 'lib/jquery', 'lib/handlebars', 'text!html/bracketTemplate.html'], function(Backbone, $, handlebars, strBracketTemplate) {
    return Backbone.View.extend({
      template: strBracketTemplate,
      initialize: function(options) {
        this.container = $("#bracketNode");
        this.model.bind('change', this.render, this);
        return this.bracketTemplate = Handlebars.compile(strBracketTemplate);
      },
      render: function() {
        var _ref;
        if ((_ref = window.console) != null) {
          _ref.log('wrote game');
        }
        $(this.el).html(this.bracketTemplate(this.model.toJSON()));
        this.container.append($(this.el));
        return this.container.append("<h4>id: " + (this.model.get('id')) + " | Name: " + (this.model.get('name')) + "</h4>");
      }
    });
  });
}).call(this);
