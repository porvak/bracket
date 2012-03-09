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
          console.log("DELETE: http://#{window.location.host + "#{jsonUri.root}/api/pool/#{jsonUri.poolId}/region/#{@get('regionId')}/game/#{@get('gameId')}/userpick/#{@get('position')}?"}\n\n")

          @set(
            userPick:null
            previousGame:null
          )

          cfg.success?()

        error:(model,response) =>
          console.log("DELETE ERROR: http://#{window.location.host + "#{jsonUri.root}/api/pool/#{jsonUri.poolId}/region/#{@get('regionId')}/game/#{@get('gameId')}/userpick/#{@get('position')}?"}\n\n")

          cfg.error?()

      })


#      'Content-Type':'application/json',
#      'Accept':'application/json, text/javascript, */*'

  )
