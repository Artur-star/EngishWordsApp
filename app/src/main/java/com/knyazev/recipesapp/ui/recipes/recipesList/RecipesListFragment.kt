package com.knyazev.recipesapp.ui.recipes.recipesList

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.knyazev.recipesapp.databinding.FragmentRecipesListBinding
import com.knyazev.recipesapp.ui.recipes.adaptersRecipes.RecipesListAdapter

class RecipesListFragment : Fragment() {
    private val recipeListFragmentArgs: RecipesListFragmentArgs by navArgs()
    private var recipesListAdapter = RecipesListAdapter(emptyList())
    private val viewModel: RecipeListViewModel by viewModels()

    private var _binding: FragmentRecipesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalArgumentException("Binding for FragmentRecipesListBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val category = recipeListFragmentArgs.categoryId
        viewModel.loadRecipesList(category)

        viewModel.recipesListStateLD.observe(viewLifecycleOwner) { (recipeList, recipeListImage, category) ->
            Log.d("!!", "$recipeList")
            try {
//                binding.ivHeaderRecipesList.setImageDrawable(recipeListImage)
                Glide.with(this)
                    .load(recipeListImage)
                    .into(binding.ivHeaderRecipesList)
                binding.tvHeaderRecipes.text = category?.title
                recipesListAdapter.updateRecipeList(recipeList)
            } catch (e: Exception) {
                Toast.makeText(
                    Application().applicationContext,
                    "Ошибка получения данных",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.rvRecipes.adapter = recipesListAdapter
        recipesListAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        findNavController().navigate(
            directions = RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(
                recipeId
            ),
        )
    }
}