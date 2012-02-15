(function() {
  define(['lib/backbone', 'lib/jquery', 'text!html/tournamentTemplate.html'], function(Backbone, $, strTournamentTemplate) {
    return Backbone.View.extend({
      initialize: function(options) {
        this.container = $("#bracketNode");
        this.model.bind('change', this.render, this);
        return this.tournamentHB = Handlebars.compile(strTournamentTemplate);
      },
      render: function() {
        $(this.el).html(this.tournamentHB(this.model.toJSON()));
        return this.container.append($(this.el));
      }
    });
  });
}).call(this);
