(function() {

  define(['lib/backbone', 'lib/jquery', 'lib/underscore', 'lib/handlebars', 'base/jsonUri', 'app/model/LeaderBoardModel', 'app/view/LeaderBoardEntryView', 'app/model/LeaderBoardCollection', 'text!html/leaderBoardTemplate.html'], function(Backbone, $, _, handlebars, jsonUri, LeaderBoardModel, LeaderBoardEntryView, LeaderBoardCollection, strLeaderBoardTemplate) {
    return Backbone.View.extend({
      initialize: function(options) {
        this.leaderBoardHB = handlebars.compile(strLeaderBoardTemplate);
        this.leaderBoardCollection = LeaderBoardCollection;
        this.leaderBoardCollection.bind('all', this.renderEntries, this);
        return this.leaderBoardCollection.fetch();
      },
      renderEntries: function() {
        var leaderBoardObj;
        this.els = this.leaderBoardCollection.map((function(entry) {
          var leaderBoardEntryView;
          leaderBoardEntryView = new LeaderBoardEntryView({
            model: entry
          });
          leaderBoardEntryView.render();
          return leaderBoardEntryView.el;
        }));
        leaderBoardObj = $(this.leaderBoardHB());
        leaderBoardObj.find('tbody').append(this.els);
        this.$el.html(leaderBoardObj);
        return this.$el.find('table').dataTable({
          "bJQueryUI": true,
          "sScrollY": "260px",
          "bPaginate": false,
          "aaSorting": [[1, 'desc'], [2, 'asc'], [0, 'asc']],
          "aoColumns": [
            null, {
              "sType": "numeric"
            }, null
          ]
        });
      },
      toggle: function() {
        if (this.$el.dialog('option', 'autoOpen')) {
          this.$el.dialog({
            autoOpen: false,
            title: 'LeaderBoard',
            height: 400,
            width: 600,
            position: ['center', 50],
            modal: true,
            resizable: false
          });
        }
        if (this.$el.dialog('isOpen')) {
          return this.$el.dialog('close');
        } else {
          return this.$el.dialog('open');
        }
      }
    });
  });

}).call(this);
