define [
  'lib/backbone'
  'base/jsonUri'
], (Backbone, jsonUri) ->
  Backbone.Model.extend(
    url: ->
      "/api/pool/#{jsonUri.poolId}/region/#{@get('regionId')}/game/#{@get('gameId')}"
  )
