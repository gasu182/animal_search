package com.example.getmyapp.ui.found

import com.example.getmyapp.ui.missing.MissingAdapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.getmyapp.R
import com.example.getmyapp.database.Pet
import com.google.firebase.database.*


class FoundFragment : Fragment() {

    private lateinit var databasePets: DatabaseReference

    private lateinit var listOfPets: ArrayList<Pet>

    private lateinit var  recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_found, container, false)

        val species = resources.getStringArray(R.array.animal_species_array)
        val speciesSpinner = root.findViewById<Spinner>(R.id.speciesSpinner)
        if (speciesSpinner != null) {
            val adapter = ArrayAdapter(requireActivity(),
                android.R.layout.simple_spinner_item, species)
            speciesSpinner.adapter = adapter
        }

        val colour = resources.getStringArray(R.array.colours_array)
        val colourSpinner = root.findViewById<Spinner>(R.id.colorSpinner)
        if (colourSpinner != null) {
            val adapter = ArrayAdapter(requireActivity(),
                android.R.layout.simple_spinner_item, colour)
            colourSpinner.adapter = adapter
        }

        val region = resources.getStringArray(R.array.regions_array)
        val regionSpinner = root.findViewById<Spinner>(R.id.regionSpinner)
        if (regionSpinner != null) {
            val adapter = ArrayAdapter(requireActivity(),
                android.R.layout.simple_spinner_item, region)
            regionSpinner.adapter = adapter
        }



        recyclerView = root.findViewById<RecyclerView>(R.id.foundPetsRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(root.context)

        databasePets = FirebaseDatabase.getInstance().getReference("Pets")

        databasePets.addValueEventListener(petListener)

        listOfPets = ArrayList<Pet>()
        return root


    }

    val petListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val pets: HashMap<String, HashMap<String, String>> = dataSnapshot.getValue() as HashMap<String, HashMap<String, String>>

            listOfPets.clear()

            for ((key, value) in pets) {
                val chipNo = value["chipNo"]
                val name = value["name"]
                val species = value["species"]
                val breed = value["breed"]
                val color = value["color"]
                val age = value["age"]
                val gender = value["gender"]
                val ownerId = value["ownerId"]
                val region = value["region"]
                val lastSeen = value["lastSeen"]
                val found = value["found"]
                if ( found != null && found.compareTo("true") == 0) {

                    if (chipNo != null && name != null && species != null && breed != null && color != null
                        && age != null && gender != null && ownerId != null && region != null && lastSeen != null
                    ) {
                        val pet: Pet = Pet(
                            key, chipNo, name, species, breed, color, age, gender,
                            ownerId, region, lastSeen, found.toBoolean()
                        )
                        listOfPets.add(pet)

                    }
                }
            }
            recyclerView.adapter = FoundAdapter(listOfPets)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed
        }
    }
}