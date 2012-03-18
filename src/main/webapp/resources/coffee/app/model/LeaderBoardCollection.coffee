define [
  'lib/backbone'
  'base/jsonUri'
  'lib/jquery'
  'app/model/LeaderBoardModel'

], (Backbone, jsonUri, $, LeaderBoardModel) ->
  new (Backbone.Collection.extend({
  url: "#{jsonUri.root}/api/pub/leaderboard/pool/4f3c8297a0eea26b78d77538"
  model: LeaderBoardModel
  }))

