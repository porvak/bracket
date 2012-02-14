(function() {
  define(['lib/backbone', 'lib/jquery'], function(Backbone, $) {
    return Backbone.View.extend({
      template: '',
      initialize: function(options) {
        this.container = $("#blah");
        return options.model.bind("change", this.render, this);
      },
      render: function() {
        $(this.el).html(this.template(this.model.toJSON()));
        this.container.append($(this.el));
        return this;
      }
    });
  });
}).call(this);
