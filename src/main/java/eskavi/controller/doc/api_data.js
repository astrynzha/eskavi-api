define({ "api": [
  {
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "optional": false,
            "field": "varname1",
            "description": "<p>No type.</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "varname2",
            "description": "<p>With type.</p>"
          }
        ]
      }
    },
    "type": "",
    "url": "",
    "version": "0.0.0",
    "filename": "./doc/main.js",
    "group": "C:\\Users\\maxig\\IdeaProjects\\api-master\\src\\main\\java\\eskavi\\controller\\doc\\main.js",
    "groupTitle": "C:\\Users\\maxig\\IdeaProjects\\api-master\\src\\main\\java\\eskavi\\controller\\doc\\main.js",
    "name": ""
  },
  {
    "type": "get",
    "url": "/imp/:id",
    "title": "Get Implementation",
    "name": "GetImplementation",
    "group": "Implementation",
    "version": "0.0.1",
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization header using the Bearer schema: Bearer token</p>"
          }
        ]
      }
    },
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "id",
            "description": "<p>Implementation unique ID</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Implementation",
            "optional": false,
            "field": "implementation",
            "description": "<p>Implementation object</p>"
          }
        ]
      }
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "type": "String",
            "optional": false,
            "field": "message",
            "description": "<p>Errormessage</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 404 Not Found\n{\n\"error\": \"UserNotFound\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./ImpController.java",
    "groupTitle": "Implementation"
  },
  {
    "type": "get",
    "url": "/imp/user",
    "title": "Get Implementations by user",
    "name": "GetImplementationByUser",
    "group": "Implementation",
    "version": "0.0.1",
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization header using the Bearer schema: Bearer token</p>"
          }
        ]
      }
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "type": "String",
            "optional": false,
            "field": "message",
            "description": "<p>Errormessage</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 404 Not Found\n{\n\"error\": \"UserNotFound\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./ImpController.java",
    "groupTitle": "Implementation"
  },
  {
    "type": "post",
    "url": "/imp",
    "title": "Post Implementation",
    "name": "PostImplementation",
    "group": "Implementation",
    "version": "0.0.1",
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization header using the Bearer schema: Bearer token</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Number",
            "optional": false,
            "field": "id",
            "description": "<p>Implementation unique ID</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1-1 201 Created\n{\n\"id\":\"1\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "type": "String",
            "optional": false,
            "field": "message",
            "description": "<p>Errormessage</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 403 Forbidden\n{\n\"error\": \"Access denied for non publishing user\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./ImpController.java",
    "groupTitle": "Implementation"
  },
  {
    "type": "put",
    "url": "/imp",
    "title": "Put Implementation",
    "name": "PostImplementation",
    "group": "Implementation",
    "version": "0.0.1",
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization header using the Bearer schema: Bearer token</p>"
          }
        ]
      }
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "type": "String",
            "optional": false,
            "field": "message",
            "description": "<p>Errormessage</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 403 Forbidden\n{\n\"error\": \"Access denied for non publishing user\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./ImpController.java",
    "groupTitle": "Implementation"
  },
  {
    "type": "delete",
    "url": "/user",
    "title": "Delete User",
    "name": "DeleteUser",
    "group": "User",
    "version": "0.0.1",
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization header using the Bearer schema: Bearer token</p>"
          }
        ]
      }
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "type": "String",
            "optional": false,
            "field": "message",
            "description": "<p>Errormessage</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 404 Not Found\n{\n\"error\": \"UserNotFound\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./UserManagementController.java",
    "groupTitle": "User"
  },
  {
    "type": "get",
    "url": "/user/security_question",
    "title": "Get security question",
    "name": "GetSecurityQuestion",
    "group": "User",
    "version": "0.0.1",
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization header using the Bearer schema: Bearer token</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "securityQuestion",
            "description": "<p>Security question to reset the password</p>"
          }
        ]
      }
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "type": "String",
            "optional": false,
            "field": "message",
            "description": "<p>Errormessage</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 401 Unauthorized\n{\n\"error\": \"Unauthorized please login to your account\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./UserManagementController.java",
    "groupTitle": "User"
  },
  {
    "type": "get",
    "url": "/user",
    "title": "Get User information",
    "name": "GetUser",
    "group": "User",
    "version": "0.0.1",
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization header using the Bearer schema: Bearer token</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "User",
            "optional": false,
            "field": "user",
            "description": "<p>User object</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Example:",
          "content": "{\n\"user\":{\n\"email\":\"test@web.de\",\n\"password\":\"$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8\",\n\"securityQuestion\":\"petName\",\n\"securityAnswer\":\"Jim\"\n\"userLevel\":\"BasicUser\"\n}\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "type": "String",
            "optional": false,
            "field": "message",
            "description": "<p>Errormessage</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 404 Not Found\n{\n\"error\": \"UserNotFound\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./UserManagementController.java",
    "groupTitle": "User"
  },
  {
    "type": "get",
    "url": "/user/levels",
    "title": "Get user levels",
    "name": "GetUserLevels",
    "group": "User",
    "version": "0.0.1",
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization header using the Bearer schema: Bearer token</p>"
          }
        ]
      }
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "type": "String",
            "optional": false,
            "field": "message",
            "description": "<p>Errormessage</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 403 Forbidden\n{\n\"error\": \"Access denied for non admin user\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n\"users\":[\n\"user\":{\n\"email\":\"test1@web.de\",\n\"password\":\"$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8\",\n\"securityQuestion\":\"petName\",\n\"securityAnswer\":\"Jim\"\n\"userLevel\":\"BasicUser\"\n},\n\"user\":{\n\"email\":\"test2@web.de\",\n\"password\":\"$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8\",\n\"securityQuestion\":\"petName\",\n\"securityAnswer\":\"Jay\"\n\"userLevel\":\"BasicUser\"\n},\n\"user\":{\n\"email\":\"test3@web.de\",\n\"password\":\"$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8\",\n\"securityQuestion\":\"petName\",\n\"securityAnswer\":\"John\"\n\"userLevel\":\"BasicUser\"\n}]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./UserManagementController.java",
    "groupTitle": "User"
  },
  {
    "type": "get",
    "url": "/user/login",
    "title": "Login for a registered User",
    "name": "Login",
    "group": "User",
    "version": "0.0.1",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "jwt",
            "description": "<p>Token to authenticate future requests</p>"
          }
        ]
      }
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "type": "String",
            "optional": false,
            "field": "message",
            "description": "<p>Errormessage</p>"
          }
        ]
      }
    },
    "parameter": {
      "fields": {
        "Request body": [
          {
            "group": "Request body",
            "type": "String",
            "optional": false,
            "field": "email",
            "description": "<p>User mail</p>"
          },
          {
            "group": "Request body",
            "type": "Sting",
            "optional": false,
            "field": "password",
            "description": "<p>User password</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n\"email\": \"test@web.de\",\n\"password\": \"12345678\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./UserManagementController.java",
    "groupTitle": "User"
  },
  {
    "type": "get",
    "url": "/user/register",
    "title": "Register a new User",
    "name": "Register",
    "group": "User",
    "version": "0.0.1",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "message",
            "description": "<p>Returns whether a request was successful</p>"
          }
        ]
      }
    },
    "parameter": {
      "fields": {
        "Request body": [
          {
            "group": "Request body",
            "type": "String",
            "optional": false,
            "field": "email",
            "description": "<p>User mail</p>"
          },
          {
            "group": "Request body",
            "type": "Sting",
            "optional": false,
            "field": "password",
            "description": "<p>User password</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n\"email\": \"test@web.de\",\n\"password\": \"12345678\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./UserManagementController.java",
    "groupTitle": "User"
  },
  {
    "type": "put",
    "url": "/user/level",
    "title": "Set user level",
    "name": "SetUserLevel",
    "group": "User",
    "version": "0.0.1",
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization header using the Bearer schema: Bearer token</p>"
          }
        ]
      }
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "type": "String",
            "optional": false,
            "field": "message",
            "description": "<p>Errormessage</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 403 Forbidden\n{\n\"error\": \"Access denied for non admin user\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./UserManagementController.java",
    "groupTitle": "User"
  }
] });
