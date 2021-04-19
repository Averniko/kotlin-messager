package com.averniko.messager.conversationlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.averniko.messager.R
import com.averniko.messager.data.conversations.ConversationsDataSource
import com.averniko.messager.data.conversations.ConversationsRepository
import com.averniko.messager.databinding.FragmentConversationListBinding

class ConversationListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentConversationListBinding>(
            inflater,
            R.layout.fragment_conversation_list,
            container,
            false
        )
        val viewModelFactory = ConversationListViewModelFactory()
        val viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ConversationListViewModel::class.java)

        val adapter = ConversationListAdapter { conversation ->
            this.findNavController().navigate(
                ConversationListFragmentDirections
                    .actionConversationListFragmentToConversationFragment(conversation.interlocutor)
            )
        }
        binding.conversationList.adapter = adapter

        viewModel.conversations.observe(viewLifecycleOwner, Observer { conversations ->
            if (conversations != null)
                adapter.data = conversations
        })

        return binding.root
    }
}