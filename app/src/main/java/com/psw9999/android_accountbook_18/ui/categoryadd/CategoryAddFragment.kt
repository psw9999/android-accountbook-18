package com.psw9999.android_accountbook_18.ui.categoryadd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.psw9999.android_accountbook_18.R
import com.psw9999.android_accountbook_18.databinding.FragmentCategoryAddBinding
import com.psw9999.android_accountbook_18.ui.categoryadd.adapter.CategoryColorAdapter
import com.psw9999.android_accountbook_18.ui.common.BaseFragment
import com.psw9999.android_accountbook_18.ui.main.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryAddFragment :
    BaseFragment<FragmentCategoryAddBinding>(R.layout.fragment_category_add) {

    companion object {
        const val IS_SPEND = "isSpend"
    }

    private val categoryAddViewModel: CategoryAddViewModel by viewModels()
    private val categoryViewModel : CategoryViewModel by activityViewModels()
    private val categoryColorAdapter by lazy { CategoryColorAdapter(activityContext) }
    private lateinit var categoryColorList: List<Int>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let { bundle ->
            categoryAddViewModel.setIsSpend(bundle.getBoolean(IS_SPEND, false))
        }
        categoryColorList =
            if (categoryAddViewModel.isSpend.value!!) activityContext.resources.getIntArray(R.array.spend_category_array).toList()
            else activityContext.resources.getIntArray(R.array.income_category_array).toList()

        categoryColorAdapter.setOnColorClickListener { selectedColor ->
            categoryAddViewModel.setSelectedColorIndex(selectedColor)
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun initViews() {
        categoryColorAdapter.setCategoryColorList(categoryColorList)
        with(binding) {
            gvCategoryColor.adapter = categoryColorAdapter

            gvCategoryColor.setOnItemClickListener { _, _, position, _ ->
                categoryAddViewModel.setSelectedColorIndex(position)
            }

            edtCategoryTitle.addTextChangedListener {
                categoryAddViewModel.setCategoryTitle(it.toString())
            }

            btnCategoryRegister.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    categoryViewModel.saveCategory(
                        isSpend = categoryAddViewModel.isSpend.value!!,
                        title = categoryAddViewModel.categoryTitle.value!!,
                        color = categoryColorList[categoryAddViewModel.selectedColorIndex.value!!]
                    )
                    categoryViewModel.getAllCategory()
                    // TODO : 프래그먼트 종료
                }
            }
        }
    }

    override fun observe() {
        binding.viewModel = categoryAddViewModel
    }

}