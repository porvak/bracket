define [
  'lib/backbone'
  'lib/jquery'
  'text!html/tournamentTemplate.html'
], (Backbone, $, strTournamentTemplate) ->
  Backbone.View.extend(
    initialize: (options) ->
      @container = $("#bracketNode")
      @model.bind('change',@render,@)
      @tournamentHB = Handlebars.compile(strTournamentTemplate);

    render: ->
      $(@el).html(@tournamentHB(@model.toJSON()))
      @container.append($(@el))
  );