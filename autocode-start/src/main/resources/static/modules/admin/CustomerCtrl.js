angular.module('myApp').controller('CustomerCtrl', function($scope, $uibModal, $http, $anchorScroll, confirmDialogs){
    $scope.pagination = {start: 0, limit: 20, maxSize: 8, currentPage: 1, limitOptions:[10,20,50,100]};
    $scope.searchForm = {};

    $scope.search = function() {
        $anchorScroll();
        $scope.promise = $http.post(
            '/admin/CustomerCtrl/search',
            {data:$scope.searchForm, extLimit:$scope.pagination}
        ).success(function(data){
            $scope.dataList = data.data;
        });
    };
    $scope.pageChanged = function() {
        $scope.pagination.start = ($scope.pagination.currentPage - 1) * $scope.pagination.limit;
        $scope.search();
    };

    $scope.clearSearch = function() {
        $scope.pagination.start = 0;
        $scope.pagination.currentPage = 1;
        $scope.searchForm = {};
        $scope.search();
    }

    $scope.edit = function(item) {
        var rv = undefined;
        if(item) {
            rv = item.id;
        }

        $uibModal.open({
            templateUrl: '/modules/admin/CustomerEditTpl.html?v=',
            controller: 'CustomerEditCtrl',
            backdrop: 'static',
            resolve: {
                id: function() {return rv;}
            }
        }).result.then(function (data) {
            if(data == 'success') {
                $scope.search();
            }
        });
    };

    $scope.del = function(item) {
        confirmDialogs.normal('确认', '确认删除 ' + item.name + '?')
            .result.then(function (btn) {
            if(btn == 'ok') {
                $scope.promise = $http.get('/admin/CustomerCtrl/delete?id='+item.id).success(function(data){
                    if(data.result == "success") {
                        $scope.$root.$broadcast('notify', {type:'success',title:'提示',info:'删除成功',timeOut:2000});
                        $scope.search();
                    }
                    else {
                        confirmDialogs.error('错误', data.info);
                    }
                });
            }
        });
    };

    $scope.search();
});
angular.module('myApp').controller('CustomerEditCtrl', function($scope, $http, $uibModalInstance, confirmDialogs,id){
    $scope.editForm = {};
    $scope.editForm.id = id;
    if(id && id > 0) {
        $scope.title = '编辑';
        $scope.promise = $http.get('/admin/CustomerCtrl/getDetailInfo?id='+id).success(function(data){
            $scope.editForm = data.data;
        });
    }
    else {
        $scope.title = '新增';
    }

    $scope.save = function() {
        $scope.promise = $http.post('/admin/CustomerCtrl/save', $scope.editForm).success(function(data){
            $uibModalInstance.close('success');
            if(data.result == 'success') {
                $scope.$root.$broadcast('notify', {type:'success',title:'提示',info:'保存成功',timeOut:2000});
            }
            else {
                confirmDialogs.error('错误', data.info);
            }
        });
    };

    $scope.cancel = function() {
        $uibModalInstance.dismiss('cancel');
    }
});
