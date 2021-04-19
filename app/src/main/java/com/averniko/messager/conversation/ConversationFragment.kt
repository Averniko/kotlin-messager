package com.averniko.messager.conversation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.averniko.messager.R
import com.averniko.messager.data.model.Message
import com.averniko.messager.databinding.FragmentConversationBinding

class ConversationFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentConversationBinding>(
            inflater,
            R.layout.fragment_conversation,
            container,
            false
        )
        val args = ConversationFragmentArgs.fromBundle(requireArguments())
        val viewModelFactory = ConversationViewModelFactory(args.interlocutor)
        val viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ConversationViewModel::class.java)

        val adapter = ConversationAdapter()
        binding.messagesList.adapter = adapter

        viewModel.conversations.observe(viewLifecycleOwner, Observer { conversations ->
            if (conversations != null) {
                val conversation = conversations.find { conversation ->
                    conversation.interlocutor == args.interlocutor
                }
                if (conversation != null)
                    adapter.data = conversation.messages
            }
        })

        binding.sendMessageButton.setOnClickListener {
            viewModel.onSendMessage(Message(sender = "kek", receiver = args.interlocutor, text = binding.messageTextInput.text.toString()))
            binding.messagesList.smoothScrollToPosition(adapter.itemCount)
        }

        return binding.root
    }
}