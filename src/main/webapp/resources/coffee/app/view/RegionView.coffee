define [
  'lib/backbone'
  'lib/jquery'
  'lib/handlebars'
  'text!html/bracketTemplate.html'
], (Backbone, $, handlebars, strBracketTemplate) ->
  Backbone.View.extend(
    template:strBracketTemplate
    initialize:(options)->
      @container = $("#bracketNode")
      @model.bind('change',@render,@)
      @bracketTemplate = Handlebars.compile(strBracketTemplate);

    render:->
      window.console?.log('wrote game')
      $(@el).html(@bracketTemplate(@model.toJSON()))
      @container.append($(@el))

      #sample debug output
      @container.append("<h4>id: #{@model.get('id')} | Name: #{@model.get('name')}</h4>")

  );