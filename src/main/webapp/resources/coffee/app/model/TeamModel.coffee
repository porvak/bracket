define [
  'lib/backbone'
  'base/jsonUri'
], (Backbone, jsonUri) ->
  Backbone.Model.extend(
    url: ->
       "#{jsonUri.root}/api/pool/#{jsonUri.poolId}/user/pick"
  )


#  {
#  "regionId" : 4,
#  "gameId" : 15,
#  "teamId" : "996d247e"
#  }
#/api/pool/{poolId}/user/pick