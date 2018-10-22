var myApp = angular.module('myApp', ['ui.bootstrap', 'ngRoute', 'ngSanitize','cgBusy', 'toaster', 'util.treeMenu','confirmDialogs', 'ac.util.AcTable','ac.util.Pagination','ngAside', 'oc.lazyLoad']);
myApp.navNames = [];
var routeProvider;
myApp.config(['$routeProvider', function($routeProvider,$routeParams) {
	$routeProvider.otherwise({
		redirectTo: '/home'
	});
	routeProvider = $routeProvider;
}]);

myApp.run(['$http', '$ocLazyLoad',function($http,$ocLazyLoad) {
	$http.post(ctx+'/sys/route').success(function(routeList) {
		myApp.routeList = routeList;
		for(var i = 0; i < routeList.length; i++) {
			var attr = routeList[i];
			myApp.navNames[attr.path] = attr.name;
			routeProvider.when(attr.path, {
				templateUrl: attr.templateUrl,
				controller: attr.ctrl,
				resolve: {
					loadMyFiles:function($ocLazyLoad, $route) {
						var path = $route.current.$$route.originalPath;
						var attr = myApp.getRouteAttrByPath(path);
						if(!attr) {
							return;
						}
						
						if(!attr.files) {
							return;
						}
						return $ocLazyLoad.load(attr.files);
//						return $ocLazyLoad.load({
//							name: attr.ctrl,
//							files: attr.files
//						})
					}
				}
			});
		}
	});
}]);

myApp.directive('acTooltip', function($document,$compile) {
	return {
		restrict: "A",
		scope: {
			acShowtip: '@',
			acTooltip: '@'
		},
		link: function(scope, element, attrs){
			var tipClassName = 'actooltip';
			tipActiveClassName = 'actooltip-show';
			scope.actipClass = [tipClassName];
			var tip = $compile('<div ng-class="actipClass">{{acTooltip}}<div class="actooltip-arrow"></div></div>')(scope);
			
			if(attrs.acTooltipPosition) {
				scope.actipClass.push('actooltip-' + attrs.acTooltipPosition);
			}
			else {
				scope.actipClass.push('actooltip-down');
			}
			$document.find('body').append(tip);
			
			element.bind('mouseover', function(e) {
				if(scope.acShowtip === 'false') {
					return;
				}
				
				tip.addClass(tipActiveClassName);
				var pos = e.target.getBoundingClientRect();
				
				var offset = {};
				var tipHeight = tip[0].offsetHeight;
				var tipWidth = tip[0].offsetWidth;
				var elWidth = pos.width || pos.right - pos.left;
	            var elHeight = pos.height || pos.bottom - pos.top;
	            var tipOffset = 10;
				
				if(tip.hasClass('actooltip-right')) {
		          offset.top = pos.top;
		          offset.left = pos.right + tipOffset;
		        }
		        else if(tip.hasClass('actooltip-left')) {
		          offset.top = pos.top;
		          offset.left = pos.left - tipWidth - tipOffset;
		        }
		        else if(tip.hasClass('actooltip-down')) {
		          offset.top = pos.top + tipHeight + tipOffset;
				  offset.left = pos.left - (tipWidth / 2) + (elWidth / 2);
		        }
		        else {
		          offset.top = pos.top - tipHeight - tipOffset;
		          offset.left = pos.left - (tipWidth / 2) + (elWidth / 2);
		        }
        
		        tip.css({
			        left: offset.left + 'px',
			        top: offset.top + 'px'
				});
			});
			element.bind('mouseout', function () {
				tip.removeClass(tipActiveClassName);
			});
			tip.bind('mouseover', function () {
				if(scope.acShowtip === 'false') {
					return;
				}
				tip.addClass(tipActiveClassName);
			});
		
			tip.bind('mouseout', function () {
				tip.removeClass(tipActiveClassName);
			});
		}
	}
});

myApp.directive('datepickerLocaldate', ['$parse', function ($parse) {
    var directive = {
        restrict: 'A',
        require: ['ngModel'],
        link: link
    };
    return directive;

    function link(scope, element, attr, ctrls) {
        var ngModelController = ctrls[0];

        // called with a JavaScript Date object when picked from the datepicker
        ngModelController.$parsers.push(function (viewValue) {
            // undo the timezone adjustment we did during the formatting
            viewValue.setMinutes(viewValue.getMinutes() - viewValue.getTimezoneOffset());
            // we just want a local date in ISO format
            return viewValue.toISOString().substring(0, 10);
        });

        // called with a 'yyyy-mm-dd' string to format
        ngModelController.$formatters.push(function (modelValue) {
            if (!modelValue) {
                return undefined;
            }
            // date constructor will apply timezone deviations from UTC (i.e. if locale is behind UTC 'dt' will be one day behind)
            var dt = new Date(modelValue);
            // 'undo' the timezone offset again (so we end up on the original date again)
            dt.setMinutes(dt.getMinutes() + dt.getTimezoneOffset());
            return dt;
        });
    }
}]);

myApp.getRouteAttrByPath = function(path) {
	for(var i = 0; i < myApp.routeList.length; i++) {
		if(myApp.routeList[i].path == path) {
			return myApp.routeList[i];
		}
	}
	return null;
}

myApp.factory('MenuService', ['$location','$rootScope','$http','$window',function($location, $rootScope, $http, $window) {
	var self;
	var menus = [];
	//请求菜单
	$http.post(ctx+'/sys/menus').success(function(data) {
		for(var i = 0; i < data.length; i++) {
			menus.push(data[i]);
		}
    });
	
	return self = {
	    menus: menus,
	    toggleSelectMenu: function(menu) {
	    	self.openedMenu = (self.openedMenu === menu ? null : menu);
	    },
	    selectMenu: function(menu) {
	    	self.openedMenu = menu;
	    },
	    isMenuSelected: function(menu) {
	    	return self.openedMenu === menu;
	    },
	    isSelected: function(menu) {
	    	var path = menu.path;
			return $location.path().indexOf(path, $location.path().length - path.length) !== -1;
	    }
	}
}]);

myApp.controller('MainCtrl', function($rootScope, $scope, $window, $http, $location, $uibModal, toaster, $timeout,$aside, MenuService,confirmDialogs){
	$rootScope.projectName = projectName;
	var layoutConst = {
		topOffset: 50,
		footerOffset: 0,
		leftOffet: 0,
		leftNavLockWidthPX : '230px',
		leftNavMinWidthPX: '60px',
		maxSmWidth: 800
	}
	$scope.pageContentStyle = {
	    "margin-top": "3px",
	    "padding-left": "3px",
	    "padding-right": "3px",
	    "margin-top": "50px",
	    "margin-right": "3px",
	    "margin-left": layoutConst.leftNavLockWidthPX
	}
	$scope.sideBodyStyle = {};
	$scope.sideMenuStyle = {}
	
	$rootScope.$on('leftNavLockChange', function(e, isLock){
		var width = (window.innerWidth > 0) ? window.innerWidth : screen.width;
    	if($scope.isLeftNavShow()) {
    		if($scope.isToggleNavMin) {
	    		$scope.pageContentStyle["margin-left"] = layoutConst.leftNavMinWidthPX;
	    	}
	    	else {
	    		$scope.pageContentStyle["margin-left"] = layoutConst.leftNavLockWidthPX;
	    	}
    	}
    	else {
    		$scope.pageContentStyle["margin-left"] = "0px";
    	}
    })
	$scope.isLeftNavShow = function() {
		var width = (window.innerWidth > 0) ? window.innerWidth : screen.width;
		return $rootScope.leftNavLock && width > layoutConst.maxSmWidth;
	}
    
	$scope.getNavName = function() {
		var navPath = $location.path();
		if(myApp.navNames[navPath]) {
			return myApp.navNames[navPath];
		}
	};
	
	angular.element($window).bind('load', function(){
		onWindowSizeChanged();
	});
	
	angular.element($window).bind('resize', function(){
		if(this.resizeTO) clearTimeout(this.resizeTO);
        this.resizeTO = setTimeout(function() {
        	onWindowSizeChanged();
        }, 50);
	});
	
	function onWindowSizeChanged() {
	    var min_height = ((window.innerHeight > 0) ? window.innerHeight : screen.height) - 1;
	    min_height = min_height - layoutConst.topOffset- layoutConst.footerOffset - 10;
	    if (min_height < 1) {
	    	min_height = 1;
	    }
	    $scope.pageContentStyle["min-height"] = min_height+"px";
	    $scope.pageContentStyle["height"] = min_height+"px";
	    $scope.sideMenuStyle["height"] = (min_height - 84)+"px";
	    
	    $rootScope.$broadcast('leftNavLockChange', $rootScope.leftNavLock);
	    $scope.$apply();
	}
	
	$scope.logout = function(logoutUrl) {
		confirmDialogs.normal('确认', '确认退出系统 ?')
		.result.then(function (btn) {
			if(btn == 'ok') {
				window.location.href = logoutUrl;
			}
		});
	}
	
	$scope.$on('notify', function(event, toastData) {
		toaster.pop(toastData.type, toastData.title, toastData.info, toastData.timeOut);
	});
	
	$rootScope.$on('$routeChangeStart', function(event, currentRoute, previousRoute) {
		$rootScope.isRouteLoading = true;
	});
    $rootScope.$on('$routeChangeSuccess', function() {
    	$timeout(function() {
    		$rootScope.isRouteLoading = false;
    	}, 50);
    });
	
    $rootScope.leftNavLock = true;
    $scope.toggleLeftNavLock = function() {
    	$rootScope.leftNavLock = !$rootScope.leftNavLock;
    	$rootScope.$broadcast('leftNavLockChange', $rootScope.leftNavLock);
    }
    $scope.toggleNavMin = function() {
    	$scope.isToggleNavMin = !$scope.isToggleNavMin;
    	if($scope.isToggleNavMin) {
    		$scope.pageContentStyle["margin-left"] = layoutConst.leftNavMinWidthPX;
    		$scope.toogleMenuTooltip('true');//让菜单显示tooltip
    	}
    	else {
    		$scope.pageContentStyle["margin-left"] = layoutConst.leftNavLockWidthPX;
    		$scope.toogleMenuTooltip('false');//让菜单隐藏tooltip
    	}
    }
    
    $scope.toogleMenuTooltip = function(showTip) {
    	for(var i = 0; i < $scope.menu.menus.length; i ++) {
    		$scope.toogleMenuTooltipCasde($scope.menu.menus[i], showTip);
    	}
    }
    
    $scope.toogleMenuTooltipCasde = function(menu, showTip) {
    	menu.showTip = showTip;
    	if(menu.subMenus && menu.subMenus.length > 0) {
	    	for(var i = 0; i < menu.subMenus.length; i++) {
	    		$scope.toogleMenuTooltipCasde(menu.subMenus[i], showTip);
	    	}
	    }
    }
    
    $scope.menu = MenuService;
	this.menuClick = function(menu) {
		$location.path(menu.path);
		if($scope.navbarCollapsed) {
			$scope.navbarCollapsed = false;
		}

		if(navSideInstance) {
			navSideInstance.close();
		}
	}
	
	this.isOpen = function(menu) {
		return MenuService.isMenuSelected(menu)
 	}
	this.toggleOpen = function(menu) {
		MenuService.toggleSelectMenu(menu);
	}
	this.isSelected = function(menu) {
		return MenuService.isSelected(menu);
	}
	
	var navSideInstance;
	$scope.openAside = function() {
		navSideInstance = $aside.open({
			template: '\
				<div class="modal-header">\
				    <h4 class="modal-title" style="line-height: 37px;">{{$root.projectName}}</h4>\
				</div>\
				<div class="modal-body" style="padding: 0px;">\
				    <ul class="docs-menu" style="margin-top: 10px;">\
				    	<div style="padding: 10px 10px 0px;">\
							<p>\
								<i class="logoText" style="color:#000">菜单功能</i>\
								<span style="font-size: 22px;cursor: pointer;" class="pull-right material-icons" ng-show="!leftNavLock" ng-click="toggleLeftNavLock()" uib-tooltip="锁定菜单" tooltip-placement="left">lock_open</span>\
							</p>\
						</div>\
						<li style="cursor: pointer;" ng-repeat="menu in menu.menus" ng-class="{\'childActive\' : isSectionSelected(menu)}">\
							<menu-link menu="menu" ng-if="menu.type === \'link\'"></menu-link>\
				             <menu-toggle menu="menu" ng-if="menu.type === \'toggle\'"></menu-toggle>\
				      	</li>\
					</ul>\
				</div>\
			',
			placement: 'left',
			windowClass: 'leftNavSide',
			backdrop: true,
			animation: true,
			controller: function($rootScope, $scope, MenuService, $uibModalInstance) {
				$scope.menu = MenuService;
				$scope.toggleLeftNavLock = function() {
					$rootScope.leftNavLock = !$rootScope.leftNavLock;
					$rootScope.$broadcast('leftNavLockChange', $rootScope.leftNavLock);
					$uibModalInstance.close();
				}
          	}
        })
	}
});