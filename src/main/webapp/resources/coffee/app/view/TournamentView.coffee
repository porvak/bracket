define [
  'lib/backbone'
  'lib/jquery'
  'text!html/bracketTemplate.html'
], (Backbone, $, strBracketTemplate) ->
  Backbone.View.extend(
    initialize: (options) ->
      @container = $("#bracketNode")
      @model.bind('change',@render,@)
      @bracketTemplate = Handlebars.compile(strBracketTemplate);

    render: ->
      $(@el).html(@bracketTemplate(@model.toJSON()))
      @container.append($(@el))
  );