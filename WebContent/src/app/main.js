let app = angular.module("myApp", [
	"ngRoute", 
	"myApp.navBar"
	
]);

const ROOT_HREF = "/PicOnClick/#/";
const login_url = 'http://localhost:8080/PicOnClick/rest/userService/login';
const register_url = 'http://localhost:8080/PicOnClick/rest/userService/user';
const image_detail_url = 'http://localhost:8080/PicOnClick/rest/photoService/detail/';
const get_picture_for_user_url = "";
const request_op_activated_url = "http://localhost:8080/PicOnClick/rest/userService/user/request/operator/";
const get_user_by_id_url = "http://localhost:8080/PicOnClick/rest/userService/user/id/";
const change_pass_url = "http://localhost:8080/PicOnClick/rest/userService/changepass";
const get_pictures_by_user_id_url = "http://localhost:8080/PicOnClick/rest/photoService/photo/author/";
const delete_operator_url = "http://localhost:8080/PicOnClick/rest/userService/user/request/operator/remove/";
const get_all_users_url = "http://localhost:8080/PicOnClick/rest/userService/users/";

let MainController = function(AuthService, $scope, $rootScope) {
	
	console.log('AuthService: ', AuthService.checkAccess());
	
	if (!AuthService.checkAccess()) {
		AuthService.redirectTo('login');
	} else {
		console.log(AuthService.getUser().name, AuthService.getUser().password);
	}
	
	$scope.helloWorld = "Hello Angular";
};

let LoginController = function(AuthService, AccountService, $location, $scope, $window) {
	$scope.user = {
		name: '',
		password: ''
	};

	$scope.errorMsg = "";

	$scope.login = function() {
		console.log('USER: ', $scope.user);
	    AccountService.login($scope.user).then((response) => {
	    	if (response.data) {
		    	let ret_user = response.data;
		    	console.log('User:', ret_user)
		    	AuthService.setUser(JSON.stringify(ret_user));
				console.log('User set.');
				$scope.errorMsg = "";
				return AuthService.redirectTo('');
			} else {
	            $scope.user.name = "";
	            $scope.user.password = "";
	            $scope.errorMsg = "Your login has failed! Wrong password / username combination!";
	            return;
			}
	    });
    }
};

let RegisterController = function($scope, AccountService, AuthService) {

	if (AuthService.checkAccess()) {
		return AuthService.redirectTo('');
	}

	$scope.user = {
		name: '',
		password: '',
		repeat_password: '',
		email: '',
		state: '',
	};

	$scope.errorMsg = "";

	$scope.register = function() {

		if ($scope.user.password != $scope.user.repeat_password) {
			$scope.errorMsg = "Passwords do not match!";
		}

		console.log('User: ', $scope.user);
		// AccountService.register($scope.user)
		// 	.then(response => {
		// 		console.log(response.data);
		// 	})
		// 	.catch(error => {
		// 		throw new Error(error);
		// 	})
	};

	$scope.resetFields = function() {
		$scope.user = {
			name: '',
			password: '',
			repeat_password: '',
			email: '',
			state: '',
		};

		$scope.errorMsg = "";
	};

};



let AuthService = function ($rootScope, $window) {
	service = {

		checkAccess: function() {
			return localStorage.getItem('token1');
		},

		redirectTo: function(page) {
			return $window.location.href = ROOT_HREF + page;
		},
		
		getUser: function() {
			return JSON.parse(this.checkAccess());
		},
		
		setUser: function(user) {
			return localStorage.setItem('token1', user);
		},
		
		isAdmin: function() {
			if (this.getUser()) {
				return this.getUser().admin;
			}
			return false;
		},

		isOperator: function() {
			if (this.getUser()) {
				return this.getUser().admin || this.getUser().operator;
			}
			return false;
		},

		isSeller: function() {
			if (this.getUser()) {
				return !!this.getUser().seller;
			}
			return false;
		},

		shouldChangePass: function() {
			if (this.getUser()) {
				return this.getUser().opRequested;
			}
			return false;
		}
	};
	return service;
};

let AccountService = function($http) {
	service = {
		register: function (user) {
			return $http.post(register_url, user);
        },
        
		login: function (user) {
			return $http.post(login_url, user);
        },

        createOperator: function(data) {
        	return $http.post(create_operator_url, data);
        }, 

        removePassChange: function(slug) {
        	return $http.get(request_op_activated_url + slug);
        },
        
        changePassword: function(data) {
        	return $http.post(change_pass_url, data);
        },

        getUserById: function(id) {
        	return $http.get(get_user_by_id_url + id);
        },

        deleteOperator: function(name) {
        	return $http.get(delete_operator_url + name);
        },

        getAllUsers: function() {
        	return $http.get(get_all_users_url);
        }
	};
	return service;
};

let ImageService = function($http) {

	service = {
		getImagesByUser: function(id, page_num) {
			return $http.get(get_pictures_by_user_id_url + id + '/' + page_num);
		}

	};

	return service;
};

let AdminController = function(AccountService, AuthService, $http, $scope) {
	if (!AuthService.isAdmin()) {
		console.log("You're not admin");
		AuthService.redirectTo('');
	}
	$scope.admin_str = "ADMINISTRATOR";
	
	console.log('NEw admin ctrl');

	$scope.operator_username = "";
	$scope.operator_password = "";
	$scope.operator_repeat_password = "";
	$scope.operator_email = "";
	$scope.operator_state = "";

	$scope.operators = [];
	
	$scope.error_msg = "";
	
	$scope.successMsg = "";
	$scope.selectedOperator = null;

	$scope.mockOperators = ['nemanja', 'sekularac', 'sloba'];

	AccountService.getAllUsers()
		.then(res => {
			console.log('New Users fetched');
			$scope.operators = res.data.filter(user => user.operator == true);
		})
		.catch(err => {
			console.log('Users not fetched');
			throw new Error(error);
		})

	$scope.testCreateOperator = function() {

		// Validation
		if ($scope.operator_password == null || $scope.operator_repeat_password == null) {
			$scope.error_msg = "Passwords shouldn't be empty!";
			return;
		}

		if ($scope.operator_password != $scope.operator_repeat_password) {
			$scope.error_msg = "Passwords should match!";
			return;
		}

		if (!$scope.operator_email) {
			$scope.error_msg = "Email is empty!"
			return;
		}

		if (!$scope.operator_state) {
			$scope.error_msg = "Country is empty!"
			return;
		}

		$scope.error_msg = ""

		
		let data = {
			name: $scope.operator_username,
			password: $scope.operator_password,
			email: $scope.operator_email,
			state: $scope.operator_state,
			weekly: 0,
			daily: 0,
			blocked: 0,
			activated: 0,
			operator: 1,
			admin: 0,
			card: '',
			seller: 0,
			pending: 0,
			// Requesting pass change
			opRequested: 1
		};
		console.log('Operator to register: ', data);

		AccountService.register(data)
			.then(response => {
				console.log('User created!');
				$scope.operators.push($scope.operator_username);
				$scope.successMsg = "User created!";
			})
			.catch(error => {
				console.log("error registering a user");
				throw new Error(error);
			})

		return;
	}

	$scope.createOperator = function() {
		
		if ($scope.operator_password != $scope.operator_repeat_password) {
			$scope.error_msg = "Passwords should match!";
			return;
		} else {
			$scope.error_msg = null;
		}
		
		AccountService.createOperator({username: $scope.operator_username, password: $scope.operator_password, email: $scope.operator_email, state: $scope.operator_state})
			.then(response => {
				console.log('Success creating operator: ',response.data);

				$scope.mockOperators.push($scope.operator_username);
				$scope.successMsg = "Successfully created operator!";

				$scope.resetFields();

			})
			.catch(error => {
				throw new Error(error);
			})
	}

	$scope.resetFields = function() {
		$scope.operator_username = "";
		$scope.operator_password = "";
		$scope.operator_repeat_password = "";
		$scope.operator_email = "";
		$scope.operator_state = "";

		$scope.error_msg = null;
	}

	$scope.addCategory = function() {
		if ($scope.category_to_add) {
			console.log('Should add category');
			$scope.category_success = "Category added!";
			$scope.category_error = "";
		} else {
			$scope.category_error = "Error with adding categories!";
			$scope.category_success = "";	
		}
	},

	$scope.deleteOperator = function() {
		$scope.selectedOperator = $scope.selectedOperator.trim();
		console.log('TRIMMED OP:', $scope.selectedOperator);
		AccountService.deleteOperator($scope.selectedOperator)
			.then(res => {
				if (res == 'true') {
					console.log('User deleted');
					$scope.operators = $scope.operators.filter(op => op.name != $scope.selectedOperator);
				} else {
					console.log('User not deleted');
				}
			})
	}
	
	$scope.category_to_add = "";
	$scope.category_error = "";
	$scope.category_success = "";
};

let OperatorController = function(AccountService, AuthService, $http, $scope) {

	if (!AuthService.isAdmin() && !AuthService.isOperator()) {
		console.log('Isadmin: ', AuthService.getUser().isAdmin, ', isOperator: ', AuthService.getUser().isOperator);
		console.log('You\'re not operator! Redirecting you to home.');
		return AuthService.redirectTo('');
	}
	
	$scope.cfg = {
		password: "",
		repeat_password: ""
	};

	$scope.errorMsg = "";

	$scope.user = AuthService.getUser();
	
	console.log('OPERATOR USER: ', $scope.user);
	
	$scope.resetFields = function() {
		$scope.cfg = {
				password: "",
				repeat_password: ""
		};	
	};

	$scope.shouldChange = AuthService.shouldChangePass();
	
	$scope.usersToApprove = [
		{
			id: 123,
			name: 'sekularac'
		},
		{
			id: 124,
			name: 'sekularacn'
		},
		{
			id: 125,
			name: 'nsemanjas'
		}
	];
	
	$scope.approveImage = function(image) {
		console.log(image, ' approved.');
		$scope.imagesToApprove = $scope.imagesToApprove.filter((img) => image.id != img.id);
		console.log('LEFTOVERS: ', $scope.imagesToApprove);
	};
    
	
	$scope.imagesToApprove = [
		{
			id: 123,
			desc: 'asdajdlja',
			src: 'https://bulma.io/images/placeholders/256x256.png',
			rating: 0,
			tags: 'nature,portrait',
			approved: 0
		},
		
		{
			id: 124,
			desc: 'szxx',
			src: 'https://bulma.io/images/placeholders/256x256.png',
			rating: 0,
			tags: 'colorful,romantic,portrait',
			approved: 0
		}
		
		,{
			id: 125,
			desc: 'zzzz',
			src: 'https://bulma.io/images/placeholders/256x256.png',
			rating: 0,
			tags: '',
			approved: 0
		}
	];

	$scope.passChange = function() {
		
		if ($scope.cfg.password === "" || $scope.cfg.repeat_password === "") {
			$scope.errorMsg = "Passwords should not be empty!";
			return;
		}
		
		if ($scope.cfg.password !== $scope.cfg.repeat_password) {
			$scope.errorMsg = "Passwords should match!";
			return;
		}
		
		$scope.errorMsg = "";
		
		console.log('Changing password.');
		let data = {
			name: $scope.user.name,
			password: $scope.cfg.password
		};
		
		console.log('Trying to change password with: ', data);
		
		AccountService.changePassword(data)
			.then(response => {
				if (response.data) {
					AccountService.removePassChange($scope.user.name + '/confirm')
						.then(res => {
							console.log('PASSWORD CHANGED AND ACCESS GRANTED');
							$scope.errorMsg = "";
							return AuthService.redirectTo('');
						})
						.catch(error => {
							console.log('SOMETHING WENT WRONG WHILE REMOVING PASS CHANGE');
							throw new Error(error);
						})
				} else {
					$scope.errorMsg = "Password not changed!";
				}
			})
			.catch(err => {
				console.log('Error while changing the password');
				throw new Error(err);
			})
		
		
		AuthService.redirectTo('');
	};

	$scope.operatorHTML = "Hello Operatorz";
	console.log('In operator');
};

let MyProfileController = function(AuthService, $scope, $http, $routeParams) {
	
	if (!AuthService.checkAccess()) {
		console.log('Not loggedin in, cannot see profile.');
		return AuthService.redirectTo('login');
	}
	
	$scope.user = AuthService.getUser();

	$scope.myProfile = true;
	
	$scope.pictures = [
		{
			desc: 'asdajdlja',
			src: 'https://bulma.io/images/placeholders/256x256.png',
			rating: 4.2,
			tags: 'nature,portrait'
		},
		
		{
			desc: 'szxx',
			src: 'https://bulma.io/images/placeholders/256x256.png',
			rating: 4.3,
			tags: 'colorful,romantic,portrait'
		}
		
		,{
			desc: 'zzzz',
			src: 'https://bulma.io/images/placeholders/256x256.png',
			rating: 5.0,
			tags: ''
		},
		{
			desc: 'asdajdlja',
			src: 'https://bulma.io/images/placeholders/256x256.png',
			rating: 4.2,
			tags: 'nature,portrait'
		},
		
		{
			desc: 'szxx',
			src: 'https://bulma.io/images/placeholders/256x256.png',
			rating: 4.3,
			tags: 'colorful,romantic,portrait'
		}
		
		,{
			desc: 'zzzz',
			src: 'https://bulma.io/images/placeholders/256x256.png',
			rating: 5.0,
			tags: ''
		}
	];

	$scope.comments = [
		{
			content: 'Sisaj ga',
		},

		{
			content: 'Pusi ga',
		},

		{
			content: 'Lomi ga',
		}

	];
	
	$scope.getPicturesForUser = function(username) {
		$http.post(get_picture_for_user_url, {user: $scope.user.name})
			.then(response => {
				console.log('Pictures gotten: ', response.data);
				$scope.pictures = response.data.images;
			})
			.catch(error => {
				throw new Error(error);
			})
	}

	// $scope.getPicturesForUser();
	
	
	$scope.getPictureTag = function(picture) {
		console.log('TAGING: ', picture);
		return getTags(picture.tags);
	}
	
	console.log('User: ', $scope.user, ' logged in.');
	
	$scope.profileMsg = "Hello Profile";
	console.log('In profile');
};

let ProfileController = function(ImageService, AccountService, AuthService, $scope, $http, $routeParams) {
	
	if (!AuthService.checkAccess()) {
		console.log('Not loggedin in, cannot see profile.');
		return AuthService.redirectTo('login');
	};
	console.log('IN PROFILE CONTROLLER, new');

	$scope.userId = $routeParams.id;
	$scope.page_num = 1;

	AccountService.getUserById($scope.userId)
		.then(res => {
			$scope.user = res.data;
			console.log('User:', $scope.user);
			
			ImageService.getImagesByUser($scope.user.id, $scope.page_num)
			.then(response => {
				$scope.loadUserPicutres = response.data;
				console.log('DATA: ', response.data);
			})
			.catch(err => {
				console.log('Cannot get images for user: ', user.id);
				throw new Error(err);
			})
		})
		.catch(error => {
			console.log('Error getting user by id');
			throw new Error(err);
			return AuthService.redirectTo('');
		})

	$scope.loadUserPicutres = "";

	



	$scope.comments = [
		{
			content: 'Sisaj ga',
		},

		{
			content: 'Pusi ga',
		},

		{
			content: 'Lomi ga',
		}

	];
	
	
	$scope.getPictureTag = function(picture) {
		console.log('TAGING: ', picture);
		return getTags(picture.tags);
	}
	
	console.log('User: ', $scope.user, ' logged in.');
	
	$scope.profileMsg = "Hello Profile";
	console.log('In profile');
};


function getTags(strTag) {
	if (strTag) { 
		return strTag.split(',');
	}
	return '';
};

let ImageDetailController = function($http, $scope, $routeParams) {
	$scope.image_id = $routeParams.id;

	$scope.image = {

	};

	$scope.loadImage = function() {
		$http({
			method: 'POST',
	        url: image_detail_url,
	        data: {id: $routeParams.id}
        }).then(response => {
        	$scope.image = response.data;
        	console.log('Image return data: ', response.data);
        }).catch((error) => {
        	console.log('Error loading image');
        	throw new Error(error);
        })
	};
};

let ApproveController = function(AuthService, $scope, $http, $routeParams) {

	if (!AuthService.isOperator()) {
		console.log('YOu shouldnt be here, redicting to home page');
		AuthService.redirectTo('');
	} else {
		console.log('Welcome to approving pictures.');
	}

	$scope.userId = $routeParams.id;

	$scope.user = {
		name: 'MockUser',
		id: $scope.userId
	};

	$scope.imagesToApprove = [
		{
			id: 123,
			desc: 'asdajdlja',
			src: 'https://bulma.io/images/placeholders/256x256.png',
			tags: 'nature,portrait',
			rating: 0
		},
		
		{
			id: 124,
			desc: 'szxx',
			src: 'https://bulma.io/images/placeholders/256x256.png',
			tags: 'colorful,romantic,portrait',
			rating: 0
		}
		
		,{
			id: 125,
			desc: 'zzzz',
			src: 'https://bulma.io/images/placeholders/256x256.png',
			tags: '',
			rating: 0
		}
	];
	
	$scope.errorMsg = "";
	
	$scope.approveUser = function() {
		for (image of $scope.imagesToApprove) {
			if (image.rating == 0) {
				$scope.errorMsg = "Some image got left unrated";
				return;
			}
		}
		
		$scope.errorMsg = "";
		console.log('IMAGAES: ', $scope.imagesToApprove);
		console.log('Submitting rating, to POST');
	}


	$scope.loadUser = function() {
		console.log('Loading user with id: ', $scope.userId);
		// if user is not for approval, redirect to home
		// if ($scope.user.approved == true) {
		// AuthService.redirectTo('');
		//}
	};

	$scope.loadUserPicutres = function() {
		console.log('Getting approval pictures from user: ', $scope.user.name);
		console.log('Getting approval pictures from userID: ', $scope.user.id);
	};

	$scope.loadUser();
	$scope.loadUserPicutres();

};

app.factory('httpPostFactory', function ($http) {
    return function (file, data, callback) {
        $http({
            url: file,
            method: "POST",
            data: data,
            headers: {
            	'Content-Type': undefined,
            	'description': 'asdsadsa',
            	'location': 'sdadsa',
            	'tags': 'asdada'
        	}
        }).success(function (response) {
            callback(response);
        });
    };
});

app.directive('myDirective', function (httpPostFactory) {
    return {
        restrict: 'A',
        scope: true,
        link: function (scope, element, attr) {

            element.bind('change', function () {
                var formData = new FormData();
                formData.append('uploadedFile', element[0].files[0]);
                httpPostFactory('http://localhost:8080/PicOnClick/rest/photoService/photo/user/admin/upload', formData, function (callback) {
                    console.log(callback);
                });
            });
        }
    };
});

let UploadController = function(AuthService, $scope) {

	console.log('IN NEW UPLOAD CONTROLLER');
	
	$scope.user = AuthService.getUser();

	$scope.photoWidth = -1;
	$scope.photoHeight = -1;
	
	$scope.photoName = "";
	
	$scope.isHD = false;
	$scope.isFullHD = false;
	$scope.is4K = false;
	
	$scope.photoSrc = "";
	
	$scope.maxImageReso = -1;

	$scope.priceHD = 0;
	$scope.priceFullHD = 0;
	$scope.price4K = 0;

	$scope.checkResolution = function(e) {
		console.log('Trying to upload');

		var f = document.getElementById('uploading_file').files[0];
		var fajl = document.getElementById('uploading_file').value;
		var idx = fajl.lastIndexOf('\\');
		var imeFajla = fajl.substring(idx + 1);
		$scope.photoName = imeFajla;
		
        var r = new FileReader();
        
        // Getting image details
        r.onload = function(e) {
        	console.log('Reader loading');
        	var imgSrc = e.srcElement.result;
        	var image = new Image();
        	image.src = imgSrc;
        	
        	image.onload = function() {
        		$scope.photoWidth = this.width;
        		$scope.photoHeight = this.height;
        		console.log($scope.photoWidth, $scope.photoHeight);
        		if ($scope.photoWidth > $scope.photoHeight) {
        	    	$scope.maxImageReso = $scope.photoWidth;
        	    } else {
        	    	$scope.maxImageReso = $scope.photoHeight;
        	    }

        	    console.log('MAX IMAGE RESO: ', $scope.maxImageReso);
        	    
        	    if ($scope.maxImageReso > 720) {
        	    	$scope.isHD = true;
        	    }
        	    
        	    if ($scope.maxImageReso > 1080) {
        	    	$scope.isFullHD = true;
        	    }
        	    
        	    if ($scope.maxImageReso > 2100) {
        	    	$scope.is4K = true;
        	    }
        	    console.log('Resolutions');
        	    console.log($scope.isHD, $scope.isFullHD, $scope.is4K);
        	}
        }
		
	    r.onloadend = function(e) {
	      var data = e.target.result;
	      
	      $scope.photoSrc = data;
	      //send your binary data via $http or $resource or do anything else with it
	      console.log('Uploading done');
	      console.log('FILENAME: ', imeFajla);
	    };
	    
	    r.readAsDataURL(f);
	};
	
	$scope.errorMsg = "";
	$scope.description = "";
	$scope.location = "";
	$scope.tags = "";

	
	$scope.form_url = "http://localhost:8080/PicOnClick/rest/photoService/photo/user/" + $scope.user.name + "/upload/" + $scope.priceHD + "/" + $scope.priceFullHD + "/" + $scope.price4K + "/" + $scope.description + "/" + $scope.location + "/" + $scope.tags;

	console.log('FORM URL: ', $scope.form_url);
	
	$scope.upload = function($event) {
		
		$scope.form_url = "http://localhost:8080/PicOnClick/rest/photoService/photo/user/" + $scope.user.name + "/upload/" + $scope.priceHD + "/" + $scope.priceFullHD + "/" + $scope.price4K + "/" + $scope.description + "/" + $scope.location + "/" + $scope.tags;
		
		console.log('URLZ: ', $scope.form_url);
		
		// var form = this;
		
		// $event.preventDefault();

		$scope.checkResolution();


		// console.log('Prevented default');
		// console.log('FORM: ', form);

		// if (!$scope.isHD) {
		// 	if ($scope.priceHD != 0) {
		// 		$scope.errorMsg = "Your picture is not HD! Set your HD price to 0!";
		// 		return;
		// 	}
		// } else {
		// 	if ($scope.priceHD < 10 || $scope.priceHD > 50) {
		// 		$scope.errorMsg = "HD Price is invalid!";
		// 		return;	
		// 	}
		// }

		// if (!$scope.isFullHD) {
		// 	if ($scope.priceFullHD != 0) {
		// 		$scope.errorMsg = "Your picture is not FullHD! Set your FullHD price to 0!";
		// 		return;
		// 	}
		// } else {
		// 	if ($scope.priceFullHD < 50 || $scope.priceFullHD > 100) {
		// 		$scope.errorMsg = "Full HD Price is invalid!";
		// 		return;	
		// 	}
		// }

		// if (!$scope.is4K) {
		// 	if ($scope.price4K != 0) {
		// 		$scope.errorMsg = "Your picture is not 4K! Set your 4K price to 0!";
		// 		return;
		// 	}
		// } else {
		// 	if ($scope.price4K < 100 || $scope.price4K > 500) {
		// 		$scope.errorMsg = "4K Price is invalid!";
		// 		return;	
		// 	}
		// }

		// if (!$scope.description) {
		// 	$scope.errorMsg = "Fill out descrption please!";
		// 	return;
		// }

		// if (!$scope.location) {
		// 	$scope.errorMsg = "Fill out location please!";
		// 	return;
		// }

		// if (!$scope.tags) {
		// 	$scope.errorMsg = "Fill out tags please!";
		// 	return;
		// }

		// $scope.errorMsg = "";

		console.log($scope.priceHD, $scope.priceFullHD, $scope.price4K);

		console.log('Should be uploading');

	};

};

app.controller('ApproveController', ApproveController);
app.controller('MainController', MainController);
app.controller('LoginController', LoginController);
app.controller('AdminController', AdminController);
app.controller('MyProfileController', MyProfileController);
app.controller('ProfileController', ProfileController);
app.controller('ImageDetailController', ImageDetailController);
app.controller('RegisterController', RegisterController);
app.controller('OperatorController', OperatorController);
app.controller('UploadController', UploadController);
app.factory('AuthService', AuthService);
app.factory('AccountService', AccountService);
app.factory('ImageService', ImageService);

let routes = function($routeProvider) {
    $routeProvider
    	.when('/', {
	        templateUrl: 'partials/main.html',
			controller: 'MainController'
    	})
    	.when('/login', {
    		templateUrl: 'partials/login.html',
    		controller: 'LoginController'
    	})
    	.when('/admin', {
    		templateUrl: 'partials/admin.html',
    		controller: 'AdminController'
    	})
    	.when('/register', {
    		templateUrl: 'partials/register.html',
    		controller: 'RegisterController'
    	})
    	.when('/operator', {
    		templateUrl: 'partials/operator.html',
    		controller: 'OperatorController'
    	})
    	.when('/myprofile', {
    		templateUrl: 'partials/myprofile.html',
    		controller: 'MyProfileController'
    	})
    	.when('/image/:id', {
    		templateUrl: 'partials/image-detail.html',
    		controller: 'ImageDetailController'
    	})
    	.when('/approve/:id', {
    		templateUrl: 'partials/approve.html',
    		controller: 'ApproveController' 
    	})
    	.when('/upload', {
    		templateUrl: 'partials/upload.html',
    		controller: 'UploadController'
    	})
    	.when('/profile/:id', {
    		templateUrl: 'partials/profile.html',
    		controller: 'ProfileController'
    	})
};


app.config(routes);