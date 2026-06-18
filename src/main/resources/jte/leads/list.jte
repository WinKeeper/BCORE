@param java.util.List<ru.mentee.power.crm.model.Lead> leads
@param ru.mentee.power.crm.model.LeadStatus currentFilter
@param String search

@template.layout.main(content = @`
    <div class="bg-white shadow rounded-lg p-4 mb-6 flex justify-between items-center">
        <form method="get" action="/leads">
            <input
                    type="text"
                    name="search"
                    value="${search}"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md"
                    placeholder="Email или компания"
            >
            <select
                    name="status"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md">
                <option value="" selected="${currentFilter == null}">
                    Все статусы
                </option>
                <option value="NEW" selected="${currentFilter == ru.mentee.power.crm.model.LeadStatus.NEW}">
                    Новый лид
                </option>
                <option value="CONTACTED" selected="${currentFilter == ru.mentee.power.crm.model.LeadStatus.CONTACTED}">
                    Связались
                </option>
                <option value="QUALIFIED" selected="${currentFilter == ru.mentee.power.crm.model.LeadStatus.QUALIFIED}">
                    Целевой
                </option>
                <option value="CONVERTED" selected="${currentFilter == ru.mentee.power.crm.model.LeadStatus.CONVERTED}">
                    Закрыт (успешно)
                </option>
            </select>
            <button
                    type="submit"
                    class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
            >
                Найти
            </button>
            <a href="/leads" class="w-full px-3 py-2 border border-gray-300 rounded-md">Сбросить</a>
        </form>

        <a href="/leads/new"
           class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600">
            + Добавить лида
        </a>
    </div>


    @if(currentFilter != null)
        <p class="text-sm text-gray-600 mb-2">Показаны лиды со статусом: ${currentFilter}</p>
    @endif


    <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-2xl font-bold mb-4">Lead List</h2>

        <table class="min-w-full bg-white border border-gray-200">
            <thead class="bg-gray-100">
            <tr>
                <th class="px-4 py-2 text-left">Email</th>
                <th class="px-4 py-2 text-left">Telephone</th>
                <th class="px-4 py-2 text-left">Company</th>
                <th class="px-4 py-2 text-left">Status</th>
                <th class="px-4 py-2 text-left">Редактировать</th>
                <th class="px-4 py-2 text-left">Удалить</th>
            </tr>
            </thead>
            <tbody>
            @for(var lead : leads)
                <tr class="border-t hover:bg-gray-50">
                    <td class="px-4 py-2">${lead.email()}</td>
                    <td class="px-4 py-2">${lead.phone()}</td>
                    <td class="px-4 py-2">${lead.company()}</td>
                    <td class="px-4 py-2">
                            <span class="px-2 py-1 rounded text-sm bg-green-100 text-green-800">
                                ${lead.status()}
                            </span>
                    </td>
                    <td class="px-4 py-2">
                        <a href="/leads/${lead.id().toString()}/edit"
                           class="text-blue-600 hover:underline">
                            Редактировать
                        </a>
                    </td>
                    <td class="px-4 py-2">
                        <form method="post" action="/leads/${lead.id().toString()}/delete">
                            <button
                                    type="submit"
                                    class="bg-red-600 text-white px-4 py-2 rounded-md hover:bg-red-600 inline"
                                    onclick="return confirm('Удалить лида ${lead.email()}?')"
                            >
                                Удалить
                            </button>
                        </form>
                    </td>
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
`)