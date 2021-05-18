package com.averniko.messenger.conversationlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import androidx.navigation.fragment.findNavController
import com.averniko.messenger.R
import com.averniko.messenger.databinding.FragmentConversationListBinding


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
                ConversationDirections
                    .actionConversationListFragmentToConversationFragment(conversation.interlocutor)
            )
        }
        binding.conversationList.adapter = adapter

        viewModel.conversations.observe(viewLifecycleOwner, Observer { conversations ->
            if (conversations != null)
                adapter.data = conversations
        })

        binding.addConversationButton.setOnClickListener {
            viewModel.onAddConversation(binding.username.text.toString())
        }

        return binding.root
    }
}