package com.psw9999.android_accountbook_18.ui.categoryadd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.psw9999.android_accountbook_18.R
import com.psw9999.android_accountbook_18.data.dto.CategoryDto
import com.psw9999.android_accountbook_18.databinding.FragmentCategoryAddBinding
import com.psw9999.android_accountbook_18.ui.categoryadd.adapter.CategoryColorAdapter
import com.psw9999.android_accountbook_18.ui.common.BaseFragment
import com.psw9999.android_accountbook_18.ui.main.CategoryViewModel
import com.psw9999.android_accountbook_18.util.toInt
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryAddFragment :
    BaseFragment<FragmentCategoryAddBinding>(R.layout.fragment_category_add) {

    companion object {
        const val IS_SPEND = "IS_SPEND"
        const val CATEGORY_ITEM = "CATEGORY_ITEM"
        val categoryTitleList = listOf(
            "수입 카테고리 추가하기",
            "지출 카테고리 추가하기",
            "수입 카테고리 수정하기",
            "지출 카테고리 수정하기"
        )
    }

    private val categoryAddViewModel: CategoryAddViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by activityViewModels()
    private val categoryColorAdapter by lazy { CategoryColorAdapter(activityContext) }
    private lateinit var categoryColorList: List<Int>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.apply {
            val categoryItem = this.getParcelable<CategoryDto>(CATEGORY_ITEM)
            if (categoryItem == null) {
                categoryAddViewModel.setIsSpend(this.getBoolean(IS_SPEND, false))
            } else {
                setBefCategoryValue(categoryItem)
            }
        }

        categoryColorAdapter.setOnColorClickListener { selectedColor ->
            categoryAddViewModel.setSelectedColorIndex(selectedColor)
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setBefCategoryValue(categoryDto: CategoryDto) {
        with(categoryAddViewModel) {
            setIsRevising(true)
            setCategoryId(categoryDto.id)
            setCategoryTitle(categoryDto.name)
            setIsSpend(categoryDto.isSpend)
            befColor = categoryDto.color
        }
    }

    private fun initColorAdapter(befColor : Int) {
        categoryColorList =
            if (categoryAddViewModel.isSpend.value!!) activityContext.resources.getIntArray(R.array.spend_category_array).toList()
            else activityContext.resources.getIntArray(R.array.income_category_array).toList()

        categoryColorAdapter.setCategoryColorList(categoryColorList)

        if (befColor != 0)
            categoryColorAdapter.setSelectedColorIndex(findSelectedColor(befColor))

        categoryColorAdapter.setOnColorClickListener { selectedColor ->
            categoryAddViewModel.setSelectedColorIndex(selectedColor)
        }

        binding.gvCategoryColor.adapter = categoryColorAdapter
    }

    private fun findSelectedColor(selectedColor : Int) : Int {
        for ((i, color) in categoryColorList.withIndex()) {
            if (selectedColor == color) {
                return i
            }
        }
        return 0
    }

    private fun initAppbar() {
        with(binding.abAddCategory) {
            setNavigationOnClickListener {
                fragmentTerminate()
            }
            title =
                categoryTitleList[(
                        (categoryAddViewModel.isRevising.value!!.toInt() shl 1) +
                                categoryAddViewModel.isSpend.value!!.toInt())]
        }
    }

    override fun initViews() {
        with(binding) {
            initColorAdapter(categoryAddViewModel.befColor)
            initAppbar()

            gvCategoryColor.setOnItemClickListener { _, _, position, _ ->
                categoryAddViewModel.setSelectedColorIndex(position)
            }

            binding.btnCategoryRegister.setOnClickListener {
                with(categoryAddViewModel) {
                    if (isRevising.value!!) {
                        categoryViewModel.updatePayment(
                            CategoryDto(
                                id = categoryId.value!!,
                                isSpend = isSpend.value!!,
                                name = categoryTitle.value!!,
                                color = categoryColorList[categoryAddViewModel.selectedColorIndex.value!!]
                            )
                        )
                    } else {
                        categoryViewModel.saveCategory(
                            isSpend = categoryAddViewModel.isSpend.value!!,
                            title = categoryAddViewModel.categoryTitle.value!!,
                            color = categoryColorList[categoryAddViewModel.selectedColorIndex.value!!]
                        )
                    }
                }
            }
        }
    }

    private fun fragmentTerminate() {
        val fragmentManager = activity!!.supportFragmentManager
        fragmentManager.beginTransaction().remove(this).commit()
        fragmentManager.popBackStack()
    }

    override fun observe() {
        binding.viewModel = categoryAddViewModel

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    categoryViewModel.isLoading.collectLatest { isLoading ->
                        if (isLoading) binding.pbCategoryLoading.visibility = View.VISIBLE
                        else binding.pbCategoryLoading.visibility = View.GONE
                    }
                }
                launch {
                    categoryViewModel.isComplete.collectLatest { isComplete ->
                        if (isComplete) {
                            categoryViewModel.setIsComplete(false)
                            fragmentTerminate()
                        }
                    }
                }
            }
        }
    }

}