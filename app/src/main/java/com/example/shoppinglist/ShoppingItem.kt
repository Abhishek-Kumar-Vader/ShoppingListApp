package com.example.shoppinglist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults.shape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ShoppingItem(val id:Int, var name: String, var quantity:Int, var isEditing: Boolean=false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ShoppingListApp(){
    var sItem by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember{ mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement= Arrangement.Center
    ){
        Button(
            onClick = { showDialog=true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ){
            Text("Add item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            items(sItem){
                item->
                if(item.isEditing){
                    ShoppingItemEditor(item=item, onEditComplete = {
                        editedName,editedQuantity ->
                        sItem=sItem.map {it.copy(isEditing = false)}
                        val editedItem = sItem.find { it.id==item.id }
                        editedItem?.let {
                            it.name=editedName
                            it.quantity=editedQuantity
                        }
                    })
                }
                else{
                    ShoppingListItem(item= item, onClickEdit = {
                        //finding the item we are editing and changing "isEditing" boolean to true
                        sItem=sItem.map{it.copy(isEditing = it.id==item.id)}
                    }, onDeleteClick = {
                        sItem=sItem-item
                    })
                }
            }
        }
    }
    if (showDialog){
        AlertDialog(onDismissRequest = { /*TODO*/ },
            confirmButton = {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Button(onClick = {
                        if(itemName.isNotBlank()){
                            val newitem=ShoppingItem(
                                id=sItem.size+1,
                                name=itemName,
                                quantity = itemQuantity.toInt()
                            )
                            sItem=sItem+newitem
                            showDialog=false
                            itemName=""
                        }
                    }) {
                        Text("Add")
                    }
                    Button(onClick = {showDialog=false}) {
                        Text("Cancel")
                    }
                }
            },
            title = { Text("Add Shopping Item")},
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName=it },
                        singleLine = true,
                        modifier= Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                        )
                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange = { itemQuantity=it },
                        singleLine = true,
                        modifier= Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
            )
    }
}

@Composable
fun ShoppingItemEditor(item:ShoppingItem, onEditComplete:(String,Int)->Unit){
    var editName by remember { mutableStateOf(item.name) }
    var editQuantity by remember { mutableStateOf(item.quantity.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row (modifier = Modifier.fillMaxWidth().background(Color.White).padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
        ){
        Column {
            BasicTextField(
                value = editName,
                onValueChange = {editName=it},
                singleLine = true,
                modifier=Modifier.wrapContentSize().padding(8.dp)
            )
            BasicTextField(
                value = editQuantity,
                onValueChange = {editQuantity=it},
                singleLine = true,
                modifier=Modifier.wrapContentSize().padding(8.dp)
            )
        }
        Button(onClick = {
            isEditing=false
            onEditComplete(editName,editQuantity.toIntOrNull() ?: 1)
        }) {
            Text("Save")
        }
    }
}

@Composable
fun ShoppingListItem(
    item:ShoppingItem,
    onClickEdit: ()-> Unit,
    onDeleteClick: ()-> Unit,
){
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)) // Apply the shape here
            .border(border = BorderStroke(2.dp, Color.Cyan), shape = RoundedCornerShape(20.dp))
            .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
//        shape= RoundedCornerShape(20) this is not working
//        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text= "Qty: ${item.quantity}", modifier = Modifier.padding(8.dp))
        Row (modifier = Modifier.padding(8.dp)){
            IconButton(onClick =onClickEdit) {
                Icon(imageVector = Icons.Default.Edit,contentDescription = null)
            }
            IconButton(onClick =onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete,contentDescription = null)
            }
        }
    }
}