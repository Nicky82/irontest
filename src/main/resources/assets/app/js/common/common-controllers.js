'use strict';

angular.module('iron-test').controller('ErrorMessageModalController', ['$scope', '$uibModalInstance',
    'errorMessage', 'errorDetails',
  function($scope, $uibModalInstance, errorMessage, errorDetails) {
    $scope.errorMessage = errorMessage;
    $scope.errorDetails = errorDetails;

    $scope.close = function () {
      $uibModalInstance.close();
    };
  }
]);
