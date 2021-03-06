package com.tnt.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.tnt.R;
import com.tnt.dboperation.DatabaseHelper;
import com.tnt.entity.TransactionType;
import android.content.Intent;
import com.tnt.utility.Validation;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO There are many cases where finish() is not called.  Ameya, make sure the finish() method is called.
 * 		Also please comment where-ever necessary along with the method comments. This is to make sure that 
 * 		any of us who reads your code will understand it
 * @author Ameya
 *
 */
public class EditTypeActivity extends OrmLiteBaseActivity<DatabaseHelper> {

    private RuntimeExceptionDao<TransactionType,Integer> transactionTypeDao;
    private static final String DUPLICATE_TYPE_NAME_ERR = "This type name already exists! Please enter some other name.";
    private static final String BLANK_CONTACT_NAME_ERR = "Transaction Type Name cannot be blank! Please enter some name.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            transactionTypeDao = getHelper().getTransactionTypeDao();
        } catch(Exception e){
            e.printStackTrace();
        }
        setContentView(R.layout.activity_edit_type);

        // Render the view
        addListToView();
    }



    public void onAddTypeClick(View view)
    {
    	// Fetch the type name
    	EditText editText = (EditText)findViewById(R.id.editTypeName);
    	String typeName = editText.getText().toString();

    	// clear the text box
    	editText.setText("");

        if(Validation.isInputBlank(typeName))
        {
            Toast blankContactNameToast = Toast.makeText(this, BLANK_CONTACT_NAME_ERR, Toast.LENGTH_LONG);
            blankContactNameToast.show();
        }
        else
        {
            if(!isTypeNameDuplicate(typeName))
            {
                saveType(typeName);
                // Render the view
                addListToView();
            }
            else
            {
                Toast duplicateTypeNameToast = Toast.makeText(this, DUPLICATE_TYPE_NAME_ERR, Toast.LENGTH_LONG);
                duplicateTypeNameToast.show();
            }
        }
    }

    private boolean isTypeNameDuplicate(String type)
    {
        List<TransactionType> types = getTypes();

        for(TransactionType transType: types)
        {
            if(transType.getTransactionType().equalsIgnoreCase(type))
                return true;
        }

        return false;
    }

    public void onDeleteClick(View view)
    {
        //  Fetch the list of ids of the checkboxes that are checked
        List<Integer> ids = getSelectedCheckboxIds();

        // Delete these ids
        deleteTypes(ids);

        // Render the view
        addListToView();
    }

    public void onDoneClick(View view)
    {
        Intent intent = new Intent(this, HomeActivity.class);
        finish();
        startActivity(intent);
    }

    private List<Integer> getSelectedCheckboxIds()
    {

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.editTypes);
        TableLayout tableLayout = (TableLayout) linearLayout.getChildAt(0);
        List<Integer> ids = new ArrayList<Integer>();

        int numOfRows = tableLayout.getChildCount();

        for(int i=0; i < numOfRows; ++i) {
            TableRow row = (TableRow)tableLayout.getChildAt(i);

            CheckBox checkBox = (CheckBox) row.getChildAt(1);

            // If checkbox is checked add its id to list
            if(checkBox.isChecked())
                ids.add(checkBox.getId());
        }

        return ids;
    }

    private void deleteTypes(List<Integer> typeIds)
    {
        transactionTypeDao.deleteIds(typeIds);
    }


    private void saveType(String typeName){
        TransactionType transactionTypeObj = new TransactionType();
        transactionTypeObj.setTransactionType(typeName);

        // persist into database
        transactionTypeDao.create(transactionTypeObj);
    }

    private void addListToView()
    {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.editTypes);

        List<TransactionType> types = getTypes();
        View view = generateListViewForTypes(types);

        // Clear previous data
        linearLayout.removeAllViews();

        // Add new data
        linearLayout.addView(view);
    }

    private List<TransactionType> getTypes()
    {
        List<TransactionType> types = transactionTypeDao.queryForAll();

        return types;
    }

    private View generateListViewForTypes(List<TransactionType> types)
    {
        // Create the table layout
        TableLayout tableLayout = new TableLayout(this);
        for(TransactionType type : types)
        {
            // Create a table row
            TableRow tableRow = new TableRow(this);

            // Create textview that has the transaction type name
            TextView textView = new TextView(this);
            textView.setText(type.getTransactionType());

            // Create a checkbox for the type entry
            CheckBox checkBox = new CheckBox(this);
            // Bind the checkbox with the id from the db
            checkBox.setId(type.getTransactionTypeId());

            // Add the 2 views into the table row
            tableRow.addView(textView);
            tableRow.addView(checkBox);

            // Add the table row to the table
            tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }

        return tableLayout;
    }

}
