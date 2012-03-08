define [
  'lib/backbone'
  'base/jsonUri'
], (Backbone, jsonUri) ->
  Backbone.Model.extend(
    url: ->
       "#{jsonUri.root}/api/pool/#{jsonUri.poolId}/user/tiebreaker"

#   {"tieBreaker": 22}

  )
