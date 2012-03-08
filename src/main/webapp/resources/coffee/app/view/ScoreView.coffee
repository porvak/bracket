define [
  'lib/backbone'
  'lib/jquery'
  'lib/handlebars'
  'text!html/scoreTemplate.html'
], (Backbone, $, handlebars, strScoreTemplate) ->
  Backbone.View.extend

    initialize: (options) ->
      @model.on('change', @update, @)
      @scoreHB = handlebars.compile strScoreTemplate
      @render()

    render: ->
      @$el = $(@scoreHB(@model.toJSON()))

      if !@model.get('tieBreaker')
        @enableEdit()
      else
        @disableEdit()

    update: ->
      @$el.html($(@scoreHB(@model.toJSON())).html())

    events:
      "click .submit-score": "submitScore"
      "click .edit-score": "enableEdit"
      "blur #combined input": "submitScore"
      "click #combined input": "enableEdit"

    submitScore: ->
      score = parseInt(@$el.find('input').val(),10)
      if score >= 0
        @$el.addClass('saving')
        @model.save(
            tieBreaker:score
          ,
            wait:true
            success: (model,response) =>
              console.log("POST: http://#{window.location.host + model.url()}\nJSON:#{JSON.stringify(model.toJSON())}\n\n")
              @$el.removeClass('saving')
            error: (model, response) =>
              @$el.removeClass('saving')
              if response.status is 404
                alert 'Please sign in using twitter.'
              if response.status is 500
                console.log("POST ERROR: http://#{window.location.host + model.url()}\nJSON:#{JSON.stringify(model.toJSON())}\n\n")
        )
        @disableEdit()


    enableEdit: (e) ->
      @$el.find('.submit-score').removeClass('hidden')
      @$el.find('.edit-score').addClass('hidden')
      input = @$el.find('input')
      input.removeAttr('disabled')
      input.removeClass('score-locked')
      input.addClass('score-unlocked')
      input.focus()

    disableEdit: (e) ->
      input = @$el.find('input')
      input.attr('disabled','disabled')
      input.addClass('score-locked')
      input.removeClass('score-unlocked')
      @$el.find('.submit-score').addClass('hidden')
      @$el.find('.edit-score').removeClass('hidden')