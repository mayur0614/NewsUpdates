package com.example.newsupdates

import android.graphics.drawable.Icon
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lint.kotlin.metadata.Visibility
import androidx.navigation.NavController
import com.example.newsupdates.roomdb.User
import com.example.newsupdates.viewmodel.UserViewModel

@Composable
fun LoginScreen(navController: NavController,userList:List<User>){



    val isLogin = remember { mutableStateOf(false) }
    if(!isLogin.value){
        LoginUser(navController,isLogin,userList)
    }else{
        SignInUser(navController,isLogin,Constant.viewModel,userList)
    }

}


@Composable
fun SignInUser(navController: NavController,isLogin: MutableState<Boolean>,viewModel: UserViewModel,userList: List<User>){
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        var username by remember { mutableStateOf("") }

        var already by remember { mutableStateOf("") }

        var password by remember { mutableStateOf("") }
        var password1 by remember { mutableStateOf("") }
        var error by remember { mutableStateOf(false) }
        var anUser by remember { mutableStateOf(false) }
        var len by remember { mutableStateOf(false) }


        var saved = listOf(
            "GENERAL", "BUSINESS", "ENTERTAINMENT", "HEALTH", "SCIENCE", "SPORTS", "TECHNOLOGIES"
        )
        val user = User(
            username,password
        )


        Text(if(!anUser) "Sign in" else already, fontSize = 24.sp)
       // Text(already)
        Image(
            painter = painterResource(R.drawable.loginimg),
            contentDescription = "Login",
            Modifier.height(150.dp).fillMaxWidth()
        )

        OutlinedTextField(
            value = username,
            singleLine = true,
            onValueChange = {
                username = it
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            label ={
                Text(
                    if(!len)"Phone number  "  else "Invalid phone nummber "
                   // else if(anUser) "Account already exist"
                )
            },
            placeholder = {
                Text("Phone number  ")
            },

            isError = len
            )
        var passwordVisible by remember { mutableStateOf(false) }
        OutlinedTextField(

            value = password,
            singleLine = true,
            onValueChange = {
                password = it
            },
            isError =  error,
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            label ={
                Text("Enter password  ")
            },
            placeholder = {
                Text("Enter Password ")
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"
                 IconButton(
                     onClick = {
                         passwordVisible = !passwordVisible
                     }
                 ) {
                     Icon(imageVector = icon, contentDescription = description)
                 }
            }

        )
        var passwordVisible1 by remember { mutableStateOf(false) }
        OutlinedTextField(
            value = password1,
            singleLine = true,
            onValueChange = {
                password1 = it
            },
            label ={
                Text("Confirm password  ")
            },
            isError =  error,
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            placeholder = {
                Text("Confirm Password ")
            },
            visualTransformation = if (passwordVisible1) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val icon = if (passwordVisible1) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisible1) "Hide password" else "Show password"
                IconButton(
                    onClick = {
                        passwordVisible1 = !passwordVisible1
                    }
                ) {
                    Icon(imageVector = icon, contentDescription = description)
                }
            }

        )
        Button(

            onClick = {
                if(username.length==10){
                    for (user in userList){
                        if(username.equals(user.username )){
                            already = "user already exist"
                            anUser = true
                        }
//                    Log.i("users list",user.username+user.password)
                    }
                }else{

                    len = true
                }


                if(!anUser){
                    if(password.isNotEmpty()){
                        if(password.length>=4){
                            if(password1 == password){
                                viewModel.upsertUser(user)
                                isLogin.value = true


                                //navController.navigate("main")
                            }else{
                                error = true
                            }
                        }else{
                            error = true
                        }
                    }else{
                        error = true
                    }
                }//anuser


            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign in")
        }
        Spacer(Modifier.height(20.dp))
        Text(modifier = Modifier.clickable {
            isLogin.value = !isLogin.value
        },
            text = "Already have an account ?Login ")
    }
}

@Composable
fun LoginUser(navController: NavController, isLogin: MutableState<Boolean>,userList: List<User>){
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var error by remember { mutableStateOf(false) }
        var len by remember { mutableStateOf(false) }
        var notUser by remember { mutableStateOf(true) }

        var rememberMe by remember { mutableStateOf(false) }

//            Text(if(notUser) "Log in" else "",
//                fontSize = if(notUser) 24.sp else 18.sp,
//                modifier = Modifier.fillMaxWidth())
        if(notUser){
            Text("Login", fontSize = 24.sp)
        }else{
            Text("cannot found an account associated with this no ", fontSize = 18.sp)
        }

        Image(
            painter = painterResource(R.drawable.loginimg),
            contentDescription = "Login",
            Modifier.height(150.dp).fillMaxWidth()
        )

        OutlinedTextField(
            value = username,
            singleLine = true,
            onValueChange = {
                username = it
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            label = {
                Text(
                    if(!len) "Phone Number" else "Invalid phone number")
            },
            placeholder = {
                Text("Enter Username ")
            },
            isError =  len ,
            )
        var passwordVisible1 by remember { mutableStateOf(false) }
        OutlinedTextField(
            value = password,
            singleLine = true,
            onValueChange = {
                password = it
            },label = {
                Text(
                    if(!error) "password" else "wrong password"
                )
            },
            isError =  error,
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            placeholder = {
                Text("Enter Password ")
            },

            visualTransformation = if (passwordVisible1) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val icon = if (passwordVisible1) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisible1) "Hide password" else "Show password"
                IconButton(
                    onClick = {
                        passwordVisible1 = !passwordVisible1
                    }
                ) {
                    Icon(imageVector = icon, contentDescription = description)
                }
            }
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = { rememberMe = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Remember Me")
        }
        Button(
            onClick = {
                if(username.length==10){
                    for (user in userList){
                        if(username.equals(user.username) && password.equals(user.password)){
                            if(password.equals(user.password)){

                                navController.navigate("main")

                            }else{

                                error =  true
                                //wrong password
                            }

                        }else if(username.equals(user.username)){
                            error = true
                        }
                    }
                }else{
                    len = true
                }


            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log in ")
        }
        Spacer(Modifier.height(20.dp))
        Text(modifier = Modifier.clickable {
            isLogin.value = !isLogin.value
        },
            text = "Don't have an account ? Sign up ")

    }
}