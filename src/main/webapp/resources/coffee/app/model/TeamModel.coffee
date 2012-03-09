define [
  'lib/backbone'
  'base/jsonUri'
], (Backbone, jsonUri) ->
  Backbone.Model.extend(
    url: ->
       "#{jsonUri.root}/api/pool/#{jsonUri.poolId}/user/pick"

    deletePick: (cfg) ->
      $.ajax({
        url: "#{jsonUri.root}/api/pool/#{jsonUri.poolId}/region/#{@get('regionId')}/game/#{@get('gameId')}/userpick/#{@get('position')}?",
        type:"DELETE"
        success: (model,response) ->
          cfg.success?()

        error:(model,response) =>

          cfg.error?()

      })


#      'Content-Type':'application/json',
#      'Accept':'application/json, text/javascript, */*'

  )
