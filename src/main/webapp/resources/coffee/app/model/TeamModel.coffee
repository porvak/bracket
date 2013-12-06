define [
  'lib/backbone'
  'base/jsonUri'
], (Backbone, jsonUri) ->
  Backbone.Model.extend(
    url: ->
       "#{jsonUri.root}/api/pool/#{jsonUri.poolId}/user/pick"

    deletePick: (cfg) ->
      url = "#{jsonUri.root}/api/pool/#{jsonUri.poolId}/region/#{@get('regionId')}/game/#{@get('gameId')}/userpick/#{@get('position')}?"

      $.ajax({
        url: url,
        type:"DELETE"
        success: (model,response) ->
#          console.log("DELETE: http://#{window.location.host + "#{url}"}\n\n")
          cfg.success?(response)

        error:(model,response) =>
#          console.log("DELETE ERROR: http://#{window.location.host + "#{url}"}\n\n")
          cfg.error?(response)
      })


#      'Content-Type':'application/json',
#      'Accept':'application/json, text/javascript, */*'

  )
