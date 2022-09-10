package com.appinesstask.views.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.appinesstask.R
import com.appinesstask.databinding.FragmentUsersContactBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersContactFragment : Fragment(R.layout.fragment_users_contact) {

    var userContactBinding: FragmentUsersContactBinding? = null
    private val userContactVm: UsersContactVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userContactBinding = FragmentUsersContactBinding.bind(view)
        userContactBinding?.vm = userContactVm
        fetchAPI()
    }

    /**
     * This method is used to hit the API's
     * */
    private fun fetchAPI() {
        if (userContactVm.contactAdapter.isEmpty())
            userContactVm.getMyList(requireView())
    }


}